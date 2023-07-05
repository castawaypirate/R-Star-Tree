import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //main
//        args = new String[1];
//        args[0]="2";
        System.out.println("welcome");
        if(args.length == 0){
            System.out.println("you need to give the number of dimensions as an argument");
        }else{
            System.out.println("dimensions: " + args[0]);
            UserInterface ui = new UserInterface();
            ui.init(args[0]);
        }

//        //tests
//        FileManager fileManager = new FileManager(2);
//        RAsteriskTree tree = new RAsteriskTree(2, fileManager);
//        LinearSearch linearSearch = new LinearSearch(fileManager);
//        long startTime;
//        long endTime;
//        long executionTime;
//
//        // linear search range query
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.45, 26.50)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.47, 26.52)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.49, 26.54)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.51, 26.56)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.53, 26.58)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.55, 26.59)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.57, 26.60)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.59, 26.61)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.60, 26.62)))));
//        linearSearch.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.61, 26.63)))));

//        // r*-tree range query
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.45, 26.50)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.47, 26.52)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.49, 26.54)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.51, 26.56)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.53, 26.58)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.55, 26.59)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.57, 26.60)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.59, 26.61)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.60, 26.62)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.search(new BoundingBox(new Point(new ArrayList<Double>(Arrays.asList(41.36, 26.15))), new Point(new ArrayList<Double>(Arrays.asList(41.61, 26.63)))), tree.getRoot());
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");


        // linear search KNN query
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 10);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 50);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 100);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 200);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 300);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 500);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 1000);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 2000);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 3000);
//        linearSearch.knnQuery(new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 4000);
//
//
//        // r*-tree KNN query
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 10, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 50, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 100, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 200, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 300, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 500, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 1000, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 2000, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 3000, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//        startTime = System.currentTimeMillis();
//        tree.knnQuery(tree.getRoot(), new Point(new ArrayList<Double>(Arrays.asList(41.39, 26.16))), 4000, null);
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
//
//
//        // r*-tree skyline query
//        startTime = System.currentTimeMillis();
//        tree.branchAndBoundSkyline();
//        endTime = System.currentTimeMillis();
//        executionTime = endTime - startTime;
//        System.out.println("Execution time: " + executionTime + " milliseconds");
    }
}