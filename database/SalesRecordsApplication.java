package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesRecordsApplication {


    /**
     * The class implements methods of the SalesRecords database interface.
     *
     * All methods of the class receive a Connection object through which all
     * communication to the database should be performed. Note: the
     * Connection object should not be closed by any method.
     *
     * Also, no method should throw any exceptions. In particular, in case
     * an error occurs in the database, then the method should print an
     * error message and call System.exit(-1);
     */

        private Connection connection;

        /*
         * Constructor
         */
        public SalesRecordsApplication(Connection connection) {
            this.connection = connection;
        }

        public Connection getConnection()
        {
            return connection;
        }

        /**
         * getStoresThatSoldFewProducts has an integer argument called numDifferentProductsSold,
         * and returns the storeID for each store in Stores for which there are fewer than
         * numDifferentProductsSold different products in Sales at that store.
         * A value of numDifferentProductsSold that’s not greater than 0 is an error.
         */

        /**
         * This solution uses an approach to concatenate the query string with parameters.
         * We also provide a getStoresThatSoldFewProducts2() function, which uses PreparedStatement.setInt()
         * to execute a query in databases.
         */

        public List<Integer> getStoresThatSoldFewProducts(int numDifferentProductsSold)
        {
            List<Integer> result = new ArrayList<Integer>();
            // your code here

            if (numDifferentProductsSold <= 0) {
                System.out.println("Received an invalid value for numDifferentProductsSold = " + numDifferentProductsSold);
                System.exit(-1);
            }

            String query =  "select * from source1.table1";

            System.out.println(query);

            Statement statement = null;
            ResultSet resultSet = null;

            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    result.add(resultSet.getInt("id"));
                    //result.add(resultSet.getString("name"));
                }

            } catch (SQLException ex) {
                // handle the exception
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("SQLException: " + ex.getMessage());

            } finally {
                // close the ResultSet and PreparedStatement with exception handling
                if(resultSet!= null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing ResultSet: " + e.getMessage());
                    }
                }

                if(statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing Statement: " + e.getMessage());
                    }
                }
            }
            // end of your code
            return result;
        }

        /**
         * An alternative solution to getStoresThatSoldFewProducts().
         * This solution uses PreparedStatement.setInt()
         * to construct a update statement to execute an UPDATE in databases.
         */

        public List<Integer> getStoresThatSoldFewProducts2(int numDifferentProductsSold)
        {
            List<Integer> result = new ArrayList<Integer>();
            // your code here

            if (numDifferentProductsSold <= 0) {
                System.out.println("ERROR: Received an invalid value for numDifferentProductsSold = " + numDifferentProductsSold);
                System.exit(-1);
            }

            String query =  "SELECT st.storeID " +
                    "FROM Stores st, Sales sa " +
                    "WHERE st.storeID = sa.storeID " +
                    "GROUP BY st.storeID " +
                    "HAVING COUNT(DISTINCT sa.productID) < ?";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, numDifferentProductsSold);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getInt("storeID"));
                }

            } catch (SQLException ex) {
                // handle the exception
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("ERROR: SQLException: " + ex.getMessage());
            } finally {
                // close the ResultSet and PreparedStatement with exception handling
                if(resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing ResultSet: " + e.getMessage());
                    }
                }

                if(preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing PrepareStatement: " + e.getMessage());
                    }
                }
            }
            // end of your code
            return result;
        }


        /**
         * updateManufacturer method has two string arguments, oldManufacturer and newManufacturer.
         * updateManufacturer should update manuf in Products for every product whose manuf value is
         * oldManufacturer, changing its manuf value to newManufacturer.
         * updateManufacturer should return the number of products whose manuf value was changed.
         */


        /**
         * This solution uses an approach to concatenate the update statement string with parameters.
         * We also provide a updateManufacturer2() function, which uses PreparedStatement.setString()
         * to construct a update statement to execute an UPDATE in databases.
         */

        public int updateManufacturer(String oldManufacturer, String newManufacturer)
        {
            // your code here; return 0 appears for now to allow this skeleton to compile.

            int result = 0;

            String productUpdate = "UPDATE Products SET manuf = '" + newManufacturer
                    + "' WHERE manuf = '" + oldManufacturer +"'";

            PreparedStatement preparedProductUpdate = null;
            try {
                preparedProductUpdate = connection.prepareStatement(productUpdate);
                result = preparedProductUpdate.executeUpdate();

            } catch (SQLException e) {
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("ERROR: UPDATE query error, check your query statement." + e.getMessage());
            } finally {
                try {
                    if (preparedProductUpdate != null) preparedProductUpdate.close();
                } catch (SQLException e) {
                    System.out.println("ERROR: SQLException in closing PrepareStatement: " + e.getMessage());
                }
            }

            return result;

            // end of your code
        }


        /**
         * An alternative solution to updateManufacturer().
         * This solution uses PreparedStatement.setString() to construct an update statement
         * to execute an UPDATE in databases.
         */

        public int updateManufacturer2(String oldManufacturer, String newManufacturer)
        {
            // your code here; return 0 appears for now to allow this skeleton to compile.

            int result = 0;

            String query = "UPDATE Products SET manuf = ? WHERE manuf = ?";

            PreparedStatement preparedStatement = null;

            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, newManufacturer);
                preparedStatement.setString(2, oldManufacturer);
                result = preparedStatement.executeUpdate();

            } catch (SQLException e) {
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("ERROR: UPDATE query error, check your query statement." + e.getMessage());
            } finally {
                try {
                    if (preparedStatement != null) preparedStatement.close();
                } catch (SQLException e) {
                    System.out.println("ERROR: SQLException in closing PrepareStatement: " + e.getMessage());
                }
            }

            return result;

            // end of your code
        }



        /**
         * fixCustomerStatus has an integer parameters, lowCustomerID.  It invokes a stored function
         * fixStatusFunction that you will need to implement and store in the database according to the
         * description in Section 5.  fixStatusFunction should have the same parameters, lowCustomerID.
         *
         * Customers has a status attribute.  fixStatusFunction will change the status for some
         * customers whose customerID is greater than or equal to lowCustomerID; Section 5 explains
         * which customer status values should be changed, and how much they should be changed.
         * The fixCustomerStatus method should return the same integer result that the fixStatusFunction
         * stored function returns.
         *
         * The fixCustomerStatus method must only invoke the stored function fixStatusFunction, which
         * does all of the assignment work; do not implement the fixCustomerStatus method using a bunch
         * of SQL statements through JDBC.
         */

        public int fixCustomerStatus (int lowCustomerID)
        {
            // There's nothing special about the name storedFunctionResult
            int storedFunctionResult = 0;

            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String query = "SELECT fixStatusFunction(" + lowCustomerID + ")";

            try {
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(query);

                if(resultSet.next()){
                    storedFunctionResult = resultSet.getInt(1);
                }

            } catch (SQLException ex) {
                // handle the exception
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("SQLException: " + ex.getMessage());
            } finally {
                // close the ResultSet and PreparedStatement with exception handling
                if(resultSet!= null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing ResultSet: " + e.getMessage());
                    }
                }

                if(preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing Statement: " + e.getMessage());
                    }
                }
            }


            // end of your code
            return storedFunctionResult;

        }




        public int fixCustomerStatus2 (int lowCustomerID)
        {
            // There's nothing special about the name storedFunctionResult
            int storedFunctionResult = 0;

            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            String query = "SELECT fixStatusFunction(?)";

            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, lowCustomerID);
                resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    storedFunctionResult = resultSet.getInt(1);
                }

            } catch (SQLException ex) {
                // handle the exception
                // It is also OK to use System.exit(-1) to handle the exceptions.
                System.out.println("SQLException: " + ex.getMessage());
            } finally {
                // close the ResultSet and PreparedStatement with exception handling
                if(resultSet!= null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing ResultSet: " + e.getMessage());
                    }
                }

                if(preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        System.out.println("ERROR: SQLException in closing Statement: " + e.getMessage());
                    }
                }
            }
            // end of your code
            return storedFunctionResult;

        }

    }


