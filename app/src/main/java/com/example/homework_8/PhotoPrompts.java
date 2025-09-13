package com.example.homework_8;

public class PhotoPrompts
{
    public static final String FactsStructure = "Return only the text of a JSON object only regarding to the following keys of the listed facts and all data types:\n " +
            "Don't keep the word json or JSON in the start. Don't ignore this instruction\n" +
            "Write the tree's name\n" +
            "Always leave space under the tree's name and go two line under for the summary. Don't ignore this instruction." +
            "\n" +
            "Write a short line that summarizes the tree up to a point\n" +
            "Always leave space under the summary and go two lines under it. Don't ignore this instruction." +
            "\n" +
            "3. First fact: The tree's average height and shape (string)\n" +
            "Make sure to leave a space under the first fact to the second one of one line. Don't ignore this instruction" +
            "\n" +
            "5. Second fact: The family the tree is related to (string)\n" +
            "Make sure to leave a space under the second fact to the third one of one line. Don't ignore this instruction." +
            "\n" +
            "6. Third fact: The area that the tree can usually be found at (string)\n" +
            "Make sure to leave a space under the third fact to the fourth one of one line. Don't ignore this instruction." +
            "\n" +
            "7. Fourth fact: The tree's capability of producing any kind of fruit (string)\n" +
            "Make sure to leave a space under the fourth fact to the fifth one of one line. Don't ignore this instruction." +
            "Also make sure to write the tree's name and the capability instead of treating the fourth fact as a question to answer" +
            "\n" +
            "8. Fifth fact: What the tree is mostly used for (string)\n" +
            "\n" +
            "Remember, the entire response must be a single, valid JSON object and not an array of objects\n" +
            "Never leave things such as moving down a line with n in the JSON object \n" +
            "Always make sure to leave one space between each fact. Don't ignore this instruction at all as it is much important \n" +
            "Make sure all of the facts are readable, and though short and direct, they are detailed and intriguing \n" +
            "Make it as a list that is readable for the user instead of cramped up and unreadable.";

    public static final String PromptNotATree = "The picture that was provided doesn't contain any kind of tree in it or either not clear enough. Take a new picture that does contain a tree so I can analyze it and give you a list of five facts about the tree.";
    public static final String PhotoPrompt = "In case that the picture doesn't contain any kind of tree or either that it's not clear enough to be detected, say the following to the user and never use the format for it: " + PromptNotATree + "Otherwise, analyze the tree in the picture and return a list of five facts about it using the following instructions: " + FactsStructure;
}
