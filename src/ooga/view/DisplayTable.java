package ooga.view;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ooga.cardtable.*;
import ooga.data.rules.Layout;
import ooga.data.style.ICoordinate;
import java.util.*;

public class DisplayTable {

    private Pane myPane;

    private double myScreenWidth;
    private double myCardHeight;
    private double myCardWidth;
    private double myCardOffset;
    private Map<String, String> myCardNameToFileName;
    private Map<String, ICoordinate> myCellNameToLocation;

    @FunctionalInterface
    interface MyFunctionalInterface {
        public void returnSelectedDisplayCell(DisplayCell selectedCell);
    }

    List<DisplayCell> myDisplayCellData = new ArrayList<>();

    MyFunctionalInterface getSelectedCell;
    DisplayCell myMovedDisplayCell;
    ICell myMover;
    ICell myDonor;
    ICell myRecipient;
    IMove myMove;

    public DisplayTable(View.TriggerMove moveLambda, Layout layout, double screenwidth) {

        myScreenWidth = screenwidth;
        myPane = new Pane();

        myCardHeight = layout.getCardHeightRatio()*screenwidth;
        myCardWidth = layout.getCardWidthRatio()*screenwidth;
        myCardOffset = layout.getUpOffsetRatio()*screenwidth;
        // TODO: get resource file from controller for myCardNameToFileName
        myCardNameToFileName = new HashMap<>();
        List<String> cardDeck = Arrays.asList( "faceDown", "AC", "2C", "3C","4C","5C","6C","7C","8C","9C","0C","JC","QC","KC","AD","2D","3D","4D","5D","6D","7D","8D","9D","0D","JD","QD","KD","AH","2H","3H","4H","5H","6H","7H","8H","9H","0H","JH","QH","KH","AS","2S","3S","4S", "5S", "6S", "7S", "8S","9S", "0S", "JS", "QS", "KS");
        for (String card: cardDeck) {
            myCardNameToFileName.put(card, card+".png");
        }
        myCellNameToLocation = layout.getCellLayout();

        getSelectedCell = (DisplayCell selectedCell) -> {
            myMovedDisplayCell = selectedCell;
            if(checkMove()) {
                moveLambda.giveIMove(myMove);
                // call lambda function given by view, which is given by controller
            }
        };
        /*
        for(String key : layout.getCellLayout().keySet()){
            Button b = new Button(key);
            double xVal = 3 * layout.getCellLayout().get(key).getX();
            double yVal = 3 * layout.getCellLayout().get(key).getY();
            myPane.getChildren().add(b);
            b.setLayoutX(xVal);
            b.setLayoutY(yVal);
        }
         */
    }

    private boolean checkMove() {
        DisplayCell intersectedCell = checkIntersections();
        if (intersectedCell != myMovedDisplayCell) {
            myMover = myMovedDisplayCell.getCell();
            myDonor = myMovedDisplayCell.getCell().findHead();
            myRecipient = intersectedCell.getCell().findLeaf();
            System.out.println("recipient ahoy:" + myRecipient);
            myMove = new Move(myDonor, myMover, myRecipient);
        }
        return intersectedCell != myMovedDisplayCell;
    }





    private DisplayCell checkIntersections() {
        boolean isIntersection = false;
        ImageView movedImage = myMovedDisplayCell.getImageView();
        for (DisplayCell dc: myDisplayCellData) {
            ImageView otherImage = dc.getImageView();
            if (!myMovedDisplayCell.getCell().getName().equals(dc.getCell().getName())) {
                isIntersection = checkIntersection(movedImage, otherImage);
            }
            if (isIntersection) {
                return dc;
            }
        }
        return myMovedDisplayCell;
    }

    private boolean checkIntersection(ImageView a, ImageView b) {
        return a != null && b != null && a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    public Pane getPane() {
        return myPane;
    }

    public void updateCells(Map<String,ICell> cellData) {
        myPane.getChildren().clear();
        List<DisplayCell> displayCellData = makeDisplayCells(cellData);
        drawDisplayCells(displayCellData);
    }

    private List<DisplayCell> makeDisplayCells(Map<String,ICell> cellData) {
        List<DisplayCell> displayCellData = new ArrayList<>();
        for (String c: cellData.keySet()) {
            displayCellData.add(makeDisplayCell(c,(Cell)cellData.get(c)));
        }
        return displayCellData;
    }

    private DisplayCell makeDisplayCell(String key, Cell cell) {
        ICoordinate icoord = myCellNameToLocation.get(key);
        double x = icoord.getX()*myScreenWidth/100.0;
        double y = icoord.getY()*myScreenWidth/100.0;
        return new DisplayCell(getSelectedCell, cell, myCardNameToFileName, new Point2D(x,y), myCardHeight, myCardWidth, myCardOffset);
    }

    private void drawDisplayCells(List<DisplayCell> DisplayCellData) {
        for (DisplayCell dc: DisplayCellData) {
            drawDisplayCell(dc);
        }
    }

    private void drawDisplayCell(DisplayCell rootDispCell) {
        if (rootDispCell.getGroup().getChildren() == null) {
            return;
        }
        myDisplayCellData.add(rootDispCell);
        myPane.getChildren().addAll(rootDispCell.getGroup().getChildren());
        for (IOffset dir: rootDispCell.getCell().getAllChildren().keySet()) {
            if (dir == Offset.NONE) {
                continue;
            }
            drawDisplayCell(rootDispCell.getAllChildren().get((Offset) dir));
        }
    }

}



