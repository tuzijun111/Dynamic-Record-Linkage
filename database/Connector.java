package database;

import java.sql.*;
import java.io.*;
import java.util.*;

public class Connector {
    /**
     * A class that connects to PostgreSQL and disconnects.
     * You will need to change your credentials below, to match the usename and password of your account
     * in the PostgreSQL server.
     * The name of your database in the server is the same as your username.
     * You are asked to include code that tests the methods of the SalesRecordsApplication class
     * in a similar manner to the sample RunFilmsApplication.java program.
     */
        public static void main(String[] args) {

            Connection connection = null;
            try {
                //Register the driver
                Class.forName("org.postgresql.Driver");
                // Make the connection.
                // You will need to fill in your real username abd password for your
                // Postgres account in the arguments of the getConnection method below.
                connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/test",
                        "binbingu",
                        "password");

                if (connection != null)
                    System.out.println("Connected to the database!");

                /* Include your code below to test the methods of the SalesRecordsApplication class
                 * The sample code in RunFilmsApplication.java should be useful.
                 * That code tests other methods for a different database schema.
                 * Your code below: */


                SalesRecordsApplication mycon = new SalesRecordsApplication(connection);


                /*******************
                 * Test of getStoresThatSoldFewProducts method
                 */

                int numDifferentProductsSold = 3;
                System.out.println("Output of getStoresThatSoldFewProducts when numDifferentStocksSold is " + numDifferentProductsSold);
                System.out.println("ID");
                System.out.println("-------");
                List<Integer> result = mycon.getStoresThatSoldFewProducts(numDifferentProductsSold);


                for (int i = 0; i < result.size(); i++) {
                    System.out.println(result.get(i));
                }

                System.out.println("----------------------------");



                 /*
                String oldManufacturer = "Google";
                String newManufacturer = "Alphabet";

                System.out.println("Output of updateManufacturer when oldManufacturer is " + oldManufacturer);
                System.out.println("and newManufacturer is " + newManufacturer);
                System.out.println("The number of products whose manuf value was changed:");
                System.out.println(mycon.updateManufacturer(oldManufacturer, newManufacturer));
                System.out.println("----------------------------");


                int lowCostomerID = 1001;

                System.out.println("Output of fixCustomerStatus when lowCustomerID is " + lowCostomerID);
                System.out.println("The total number of customers whose status was updated:");
                System.out.println(mycon.fixCustomerStatus(lowCostomerID));
                System.out.println("----------------------------");


                */

            }
            catch (SQLException | ClassNotFoundException e) {
                System.out.println("Error while connecting to database: " + e);
                e.printStackTrace();
            }
            finally {
                if (connection != null) {
                    // Closing Connection
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("Failed to close connection: " + e);
                        e.printStackTrace();
                    }
                }
            }
        }
    }




