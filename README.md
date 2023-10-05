# R*-Tree
This project is an R*-Tree implementation in java. R*-Trees are an extension and improvement of the R-Tree, designed to overcome some of its limitations, store and manage multidimensional spatial data more efficiently.
## Data
The data can be inserted either by adding individual sets of coordinates one at a time or by bulk-inserting data from a CSV file. The project also supports OSM files, which are converted to CSV using the `BasicOSMParser`(https://github.com/PanierAvide/BasicOSMParser/tree/master). The CSV or OSM files should be stored in a directory called `resources` which should be in the same directory as the executable.
## Usage
The project should be built into a JAR before it is executed. To do so, import the BasicOSMParser.jar (https://github.com/PanierAvide/BasicOSMParser/blob/master/BasicOSMParser.jar). The program takes the dimensions of the coordinates as a **parameter**.
## Storage
The raw data is stored inside the blocks of the `datafile.dat` and the nodes which represent the structure of the tree are stored inside the `indexfile.dat`.
## Functionalities :
1. Insert
2. Delete
3. Range Query
4. KNN Query
5. Skyline Query
6. Bulk Loading
## References
- Antonin Guttman. R-TREES: A DYNAMIC INDEX STRUCTURE FOR SPATIAL SEARCHING.
- Norbert Beckmann, Hans-Peter Knegel, Ralf Schneider, Bernhard Seeger. The R*-tree: An Efficient and Robust Access Method for Points and Rectangles.
- Nick Roussopoulos, Stephen Kelley, Frederic Vincent. Nearest Neighbor Queries.
- Jianzhong Qi, Yufei Tao, Yanchuan Chang, Rui Zhang. Packing R-trees with Space-filling Curves: Theoretical Optimality, Empirical Efficiency, and Bulk-loading Parallelizability.
