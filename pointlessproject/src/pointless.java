// Owen Bedford
// 26/10/2020
// Version 1
// Level 4
// Pointless is a game, where a user needs to enter an answer and the goal is to choose the answer the least amount of people would say. This program simulates this game.


import java.util.Random;
import java.util.Scanner;
import java.io.*;

class pointless {

    //Main method
    public static void main (String [] a) throws IOException {



        final int NUMOFPEOPLE = 4;
        final int NUMOFQUEST = 2;
        final String BASEFILENAME = "game";
        int gamecount = 1;
        String answer = "";
        QuestionDatabase questiondatabase = createQuestionDatabase(NUMOFQUEST);
        Player [] players = new Player[NUMOFPEOPLE];
        createQuestion(questiondatabase, "Name a animated movie that has received a award.", "Snow White", "Spirited Away", "Your Name", 70, 50, 3, 1);
        createQuestion(questiondatabase, "Name a UK olympic track and field gold medal winner.", "Dina Asher_Smith", "Sebastian Coe", "Fatima Whitbread", 78, 42, 2, 2);

        String roundcont = "Y";

        while(!roundcont.equals("N")) //Loops until a user says stop
        {

            for(int i=1; i<=NUMOFPEOPLE; i++) //Loops for the number of people playing
            {
                Random random = new Random();

                int randomnum = random.nextInt(NUMOFQUEST); //Generates a random number
                int score = 0;
                System.out.println();
                players[i-1] = createPlayer(i);
                answer = enterAnswer(i, questiondatabase, randomnum);
                System.out.println();
                score = checkAnswer(answer, score, questiondatabase, randomnum);
                setPlayerScore(players, i, score);
            }
            System.out.println();
            printScores(players, NUMOFPEOPLE, gamecount, BASEFILENAME);
            System.out.println();
            roundcont = inputString("Do you want to continue for another round? (Y/N): ");
            gamecount = gamecount + 1;
        }

        System.out.println();
        doResults(BASEFILENAME, gamecount, NUMOFPEOPLE);
        System.out.println("Thank you for playing!");


        System.exit(0);
    }

    //Gets String input from keyboard
    public static String inputString(String message)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print(message);
        return scanner.nextLine();
    }

    //Gets int input from keyboard
    public static int inputInt(String message)
    {
        return Integer.parseInt(inputString(message));
    }

    //Returns an answer, which is used within other methods
    public static String enterAnswer(int person, QuestionDatabase questiondatabase, int randomnum)
    {

        System.out.println(getQuestions(questiondatabase, randomnum));

        String answer = inputString("Player " + person + ", please enter which option the fewest people said. ");
        return answer;
    }

    //Used to check the answer, and assign a score based on the answer, the variable score is returned
    public static int checkAnswer(String answer, int score, QuestionDatabase questiondatabase, int randomnum)
    {

        if(answer.equals(getAnswerHighest(questiondatabase, randomnum)))
        {
            score = getScoresHighest(questiondatabase, randomnum);

        }

        else if (answer.equals(getAnswerMid(questiondatabase, randomnum)))
        {
            score = getScoresMid(questiondatabase, randomnum);

        }

        else if (answer.equals(getAnswerLowest(questiondatabase, randomnum)))
        {
            score = getScoresLowest(questiondatabase, randomnum);

        }

        else
        {
            System.out.println("Nobody said " + answer);
            score = 100;
        }

        return score;

    }

    //Reads a file which stores the results from a round if a user wants to know the results
    public static void doResults(String BASEFILENAME, int gamecount, int NUMOFPEOPLE)
            throws IOException
    {
        String seeresult = askResult();
        while(seeresult.equals("Y") | seeresult.equals("Yes") | seeresult.equals("yes"))
        {
            int roundnumber = roundNumberValidation(gamecount);
            String filename = BASEFILENAME + roundnumber;

            BufferedReader inputStream = new BufferedReader(new FileReader(filename + ".txt"));


            System.out.println("From best player to worst player: ");
            for(int i=1; i<=NUMOFPEOPLE; i++)
            {
                String s = inputStream.readLine();
                System.out.println(s);
            }

            System.out.println();
            inputStream.close();

            seeresult = askResult(); //Checks if they want to find out more results
        }
    }

    //Validation to ensure a round number is entered correctly
    public static int roundNumberValidation(int GAMECOUNT)
    {
        int roundnumber = inputInt("What round number do you want to see the results for: ");
        while(!(roundnumber<GAMECOUNT && roundnumber>0))
        {
            roundnumber = inputInt("You entered a number for a round which doesn't exist. The amount of rounds that occured were " + (GAMECOUNT-1) + ". Enter a different number: ");
        }
        System.out.println();
        return roundnumber;
    }

    //Returns a string, which is used to determine if a boolean expression evaluates to true or false
    public static String askResult()
    {
        String holder = inputString("Thank you for playing pointless, would you like to see the result of a specific round? (Y/N): ");

        return holder;
    }

    //Sorts an array of records in order, from best player to worst
    public static void sortPlayerArray(Player [] players)
    {
        boolean inorder = false;
        while(!inorder)
        {
            inorder = true;
            for(int i=0; i< players.length-1; i++)
            {
                if(players[i].score>players[i+1].score)
                {
                    Player holder = players[i+1];
                    players[i+1] = players[i];
                    players[i] = holder;
                    inorder = false;
                }
            }
        }
    }


    /*************************************************
     ** Following methods are for QuestionDatabase Abstract Data Type
     ** These are the only ways that QuestionDataBase should be accessed
     * Create empty QuestionDataBase
     * Add a Question to QuestionDataBase
     ***************************************************/


    //Used to initialise variables of the type QuestionDatabase
    public static QuestionDatabase createQuestionDatabase(int NUMOFQUEST)
    {
        QuestionDatabase database = new QuestionDatabase();
        Question[] s = new Question[NUMOFQUEST];
        database.questiondatabase = s;
        return database;
    }

    /*************************************************
     **Following methods for Question Abstract Data Type
     **The only way that Question should be accessed
     * To create a question
     * Getting question scores
     * Getting question answers
     ***************************************************/


    //Method used to create questions which are then stored within a array of the type Scores
    public static void createQuestion(QuestionDatabase q, String question, String answer, String answer2, String answer3, int score, int score2, int score3, int questionnum)
    {
        Question s = new Question();

        if(questionnum<=q.questiondatabase.length) //Checks that questiondatabase isn't full
        {
            s.question = question;
            s.answer = answer;
            s.answer2 = answer2;
            s.answer3 = answer3;
            s.score = score;
            s.score2 = score2;
            s.score3 = score3;
            q.questiondatabase[questionnum-1] = s;
        }

        return;

    }

    //Used to get one of the questions
    public static String getQuestions(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].question;
    }

    //Used to get one of the scores
    public static int getScoresHighest(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].score;
    }

    //Used to get one of the scores
    public static int getScoresMid(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].score2;
    }

    //Used to get one of the scores
    public static int getScoresLowest(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].score3;
    }

    //Used to get one of the answers
    public static String getAnswerHighest(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].answer;
    }

    //Used to get one of the answers
    public static String getAnswerMid(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].answer2;
    }

    //Used to get one of the answers
    public static String getAnswerLowest(QuestionDatabase q, int rand)
    {
        return q.questiondatabase[rand].answer3;
    }

    /*************************************************
     **Following methods for Player Abstract Data Type
     **The only way that Player should be accessed
     * To create a Player
     * To set a Player score
     * Printing the score of a player, alongside name and contestant number from best to worst
     ***************************************************/

    //Creates a person, where a name, default score and candidate number are all set at once.
    public static Player createPlayer(int i)
    {
        Player player = new Player();
        System.out.println("Welcome to pointless contestant " + (i));
        player.name = inputString("Tell us your name: ");
        player.score = 100; //Default score
        player.contestantnumber = (i-1);
        return player;
    }

    //Sets a Person score and also adds a person to the QuestionDatabase
    public static void setPlayerScore(Player [] players, int i, int score)
    {
        Player player = players[i-1]; //Subtract 1 because i starts counting from 1
        player.score = score;
        players[i-1] = player;
    }

    //Prints all scores for the players in a given round & sorts the scores also writes to a text file for that round
    public static void printScores(Player[] players, int MAXPLAYERS, int gamecount, String BASEFILENAME)
        throws  IOException
    {
        String filename = BASEFILENAME + gamecount;

        PrintWriter outputStream = new PrintWriter(new FileWriter(filename + ".txt"));
        System.out.println("Pointless measures score from lowest (best) to highest (worst). The following is the league table of this round from best to worst.");
        sortPlayerArray(players);
        for(int i = 0; i<MAXPLAYERS; i++)
        {
            Player player = players[i];
            System.out.println("Player " + (player.contestantnumber+1) + " named " + player.name + " score is " + player.score);
            outputStream.println("Player " + (player.contestantnumber+1) + " named " + player.name + " score is " + player.score);
        }
        outputStream.close();

        return;
    }


}

//Class containing fields related to the game pointless
class Question
{
    String question;
    String answer;
    String answer2;
    String answer3;
    int score;
    int score2;
    int score3;
}

class Player
{
    String name;
    int score;
    int contestantnumber;
}

//Class QuestionDatabase
class QuestionDatabase
{
    Question [] questiondatabase;
}
