package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class HandleFiles {

    private final static String whitelistLocation = "whitelist.json";
    private final static String playerJoinTriesLocation = "playerJoinTries.json";
    private final static String passwordLocation = "customWhitelistPassword.txt";


    private static String openFile(String filename) {
        String fileContent = "";
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                fileContent = fileContent + line + System.lineSeparator();
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException fileNotFoundException) {
            if (filename == whitelistLocation) noWhitelist();
            else if (filename == playerJoinTriesLocation) noPlayerJoinTries();
            else if (filename == passwordLocation) noPasswordLocation();
            else throw new RuntimeException(fileNotFoundException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
        return fileContent;
    }

    private static void noWhitelist() {
        Bukkit.broadcastMessage(ChatColor.RED + "NO WHITELIST DETECTED. SERVER IS STOPPING.\nPLEASE MESSAGE AN ADMINISTRATOR");
        Bukkit.shutdown();
    }

    private static void noPlayerJoinTries() {
        File file = new File(playerJoinTriesLocation);
        writeToFile(playerJoinTriesLocation, "[]");
    }

    private static void noPasswordLocation() {
        File file = new File(passwordLocation);
        writeToFile(passwordLocation, "join");

    }

    private static void writeToFile(String filename, String content) {
        try {
            FileWriter writer = new FileWriter(filename);

            writer.write(content);
            writer.close();
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static boolean isPlayerOnWhitelist(String playerName) {
        String content = openFile(whitelistLocation);
        if (content.contains(playerName)) return true;
        return false;
    }

    public static boolean isPlayerOnJoinList(String playerName) {
        String content = openFile(playerJoinTriesLocation);
        if (content.contains(playerName)) return true;
        return false;
    }

    public static boolean isPlayerStatusBanned(String playerName) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                if (line.contains(playerName)) {
                    br.readLine();
                    if (br.readLine().contains("banned")) {
                        fr.close();
                        br.close();
                        return true;
                    }
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ignore) {
            // Ignored
        } catch (IOException ioException) {
            //TODO: Handle IOException
        }
        return false;
    }

    public static void putNameOnWhitelist(String playerName) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "whitelist add " + playerName);
    }

    public static int handleJoinTries(String playerName) {
        int triesRemaining = 4;

        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);
            String content = "";

            String line = br.readLine();
            while (line != null) {
                content = content + line + System.lineSeparator();

                if (line.contains(playerName)) {
                    line = br.readLine();

                    String cString = line.replace("\"tries\": \"", "");
                    cString = cString.replaceAll("\",", "");
                    cString = cString.replaceAll("\\s+", "");

                    int cInt = Integer.parseInt(cString);

                    line = line.replace(Integer.toString(cInt), Integer.toString(cInt + 1));
                    content = content + line + System.lineSeparator();
                    triesRemaining = 3 - cInt;
                }
                line = br.readLine();
            }

            writeToFile(playerJoinTriesLocation, content);

            br.close();
            fr.close();
        } catch (FileNotFoundException ignore) {

        } catch (NumberFormatException numberFormatException) {
            throw new RuntimeException(numberFormatException);
            // TODO: Handle NumberFormatException
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
            // TODO: Handle IOException
        }
        return triesRemaining;
    }

    public static void updateTries(String playerName, int tries) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            String content = "";

            String line = br.readLine();
            while (line != null) {
                content = content + line + System.lineSeparator();

                if (line.contains(playerName)) {
                    line = br.readLine();

                    String cString = line.replace("\"tries\": \"", "");
                    cString = cString.replaceAll("\",", "");
                    cString = cString.replaceAll("\\s+", "");

                    int cInt = Integer.parseInt(cString);

                    line = line.replace(Integer.toString(cInt), Integer.toString(tries));
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            writeToFile(playerJoinTriesLocation, content);

            br.close();
            fr.close();
        } catch (IOException fileNotFoundException) {
            throw  new RuntimeException(fileNotFoundException);
        }
    }

    private static @NotNull String resetTries(String content, String line) {
        // Reset tries and write to content
        String cString = line;
        cString = cString.replace("\"tries\":", "");
        cString = cString.replaceAll("\"", "");
        cString = cString.replaceAll("\\s+", "");
        line = line.replace(cString, "0");
        content = content + line + System.lineSeparator();

        return content;
    }

    public static void handlePutPlayerOnTries(String player) {
        String template = templateHandling(player);
        writePlayerToJoinTries(template);
    }

    public static boolean isPlayerOnTries(String player) {
        if (openFile(playerJoinTriesLocation).contains(player)) return true;
        return false;
    }

    public static @NotNull String templateHandling(String name) {
        String template = "\t{" + System.lineSeparator() + "\t\t\"name\": \"putNameHere\"," + System.lineSeparator() + "\t\t\"tries\": \"0\"," + System.lineSeparator() + "\t\t\"status\": \"newPlayer\"" + System.lineSeparator() + "\t}";

        template = template.replace("putNameHere", name);

        return template;
    }

    public static void writePlayerToJoinTries(String template) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);
            String newOutput = "";
            String newLine = br.readLine();

            while (newLine != null) {
                newOutput = newOutput + newLine + System.lineSeparator();
                newLine = br.readLine();
            }

            newOutput = newOutput.replace("[", "[" + System.lineSeparator() + template + System.lineSeparator());

            writeToFile(playerJoinTriesLocation, newOutput);
            br.close();
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updatePlayerStatusToOnWhitelist(String playerName) {
        try {
            //Open File
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            //Find correct line and reset tries + change status
            String content = "";
            String line = br.readLine();

            while (line != null) {
                content = content + line + System.lineSeparator();
                if (line.contains(playerName)) {
                    line = br.readLine();

                    // Reset tries and write to content
                    content = resetTries(content, line);

                    line = br.readLine();

                    // Change status and write to content
                    line = line.replace("newPlayer", "onWhitelist");
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            // Write new content to file
            writeToFile(playerJoinTriesLocation, content);
            br.close();
            fr.close();

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static void updatePlayerStatusToVerified(String playerName) {
        try {
            //Open File
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            //Find correct line and reset tries + change status
            String content = "";
            String line = br.readLine();

            while (line != null) {
                content = content + line + System.lineSeparator();
                if (line.contains(playerName)) {
                    line = br.readLine();

                    content = resetTries(content, line);

                    line = br.readLine();

                    // Change status and write to content
                    line = line.replace("onWhitelist", "verified");
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            // Write new content to file
            FileWriter writer = new FileWriter(playerJoinTriesLocation);
            writer.write(content);

            // Close Reader/Writer
            writer.close();
            br.close();
            fr.close();

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static void updatePlayerStatusToBanned(String playerName) {
        try {
            //Open File
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            //Find correct line and reset tries + change status
            String content = "";
            String line = br.readLine();

            while (line != null) {
                content = content + line + System.lineSeparator();
                if (line.contains(playerName)) {
                    line = br.readLine();

                    content = resetTries(content, line);

                    line = br.readLine();

                    // Change status and write to content
                    if (line.contains("newPlayer")) line = line.replace("newPlayer", "banned");
                    else if (line.contains("onWhitelist")) line = line.replace("onWhitelist", "banned");
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            // Write new content to file
            writeToFile(playerJoinTriesLocation, content);
            br.close();
            fr.close();

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static void updatePlayerStatusToNewPlayer(String playerName) {
        try {
            //Open File
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            //Find correct line and reset tries + change status
            String content = "";
            String line = br.readLine();

            while (line != null) {
                content = content + line + System.lineSeparator();
                if (line.contains(playerName)) {
                    line = br.readLine();

                    content = resetTries(content, line);

                    line = br.readLine();

                    // Change status and write to content
                    if (line.contains("banned")) line = line.replace("banned", "newPlayer");
                    else if (line.contains("verified")) line = line.replace("verified", "newPlayer");
                    else if (line.contains("onWhitelist")) line = line.replace("onWhitelist", "newPlayer");
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            // Write new content to file
            FileWriter writer = new FileWriter(playerJoinTriesLocation);
            writer.write(content);

            // Close Reader/Writer
            writer.close();
            br.close();
            fr.close();

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    public static String getPlayerStatus(String playerName) {

        try {
            //Open File
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            //Find correct line and reset tries + change status
            String line = br.readLine();

            while (line != null) {
                if (line.contains(playerName)) {
                    line = br.readLine();
                    line = br.readLine();

                    line = line.replace("\"status\":", "");
                    line = line.replaceAll("\\s+", "");
                    line = line.replaceAll("\"", "");
                    return line;
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException fileNotFoundException) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "error";
    }

    public static void setPassword(String password) {
        writeToFile(passwordLocation, password);
    }

    public static String getPassword() {
        try {
        FileReader fr = new FileReader(passwordLocation);
        BufferedReader br = new BufferedReader(fr);
            try {
                return br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            noPasswordLocation();
        }
        return "join";
    }
}
