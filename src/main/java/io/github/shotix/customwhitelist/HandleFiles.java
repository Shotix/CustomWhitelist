package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class HandleFiles {

    private final static String whitelistLocation = "whitelist.json";
    private final static String playerJoinTriesLocation = "playerJoinTries.json";
    private static String whitelistedPlayerNames = "";
    private final static String wLNW = "WhitelistNotFound";
    private final static String templateLocation = "playerJoinTriesTemplate.json";


    public static String openWhitelist() {
        try {
            FileReader fr = new FileReader(whitelistLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                if (line.contains("name")) {
                    line = line.substring(13);
                    line = line.replaceAll("\"", "");
                    whitelistedPlayerNames = whitelistedPlayerNames + line + ";";
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
            return whitelistedPlayerNames;
        }
        catch (FileNotFoundException fnf) {
            return wLNW;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPlayerOnJoinList(String playerName) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                if (line.contains(playerName)) {
                    br.readLine();
                    if (br.readLine().contains("verified")) {
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

            FileWriter writer = new FileWriter(playerJoinTriesLocation);
            writer.write(content);

            writer.close();
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
        } catch (FileNotFoundException fileNotFoundException) {
            throw  new RuntimeException(fileNotFoundException);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handlePutPlayerOnTries(String player) {
        String template = templateHandling(player);
        writePlayerToJoinTries(template);
    }

    public static boolean isPlayerOnTries(String player) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            while (line != null) {
                if (line.contains(player)){
                    br.close();
                    fr.close();
                    return true;
                }
                line = br.readLine();
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException fileNotFoundException) {
            try {
                File file = new File(playerJoinTriesLocation);
                FileWriter writer = new FileWriter(playerJoinTriesLocation);
                writer.write("[]");
                writer.close();
                isPlayerOnTries(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public static String templateHandling(String name) {
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

            FileWriter writer = new FileWriter(playerJoinTriesLocation);

            newOutput = newOutput.replace("[", "[" + System.lineSeparator() + template + System.lineSeparator());

            writer.write(newOutput);
            writer.close();
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
                content = content + line + System.lineSeparator();;
                if (line.contains(playerName)) {
                    line = br.readLine();

                    // Reset tries and write to content
                    String cString = line;
                    cString = cString.replace("\"tries\":", "");
                    cString = cString.replaceAll("\"", "");
                    cString = cString.replaceAll("\\s+", "");
                    line = line.replace(cString, "0");
                    content = content + line + System.lineSeparator();

                    line = br.readLine();

                    // Change status and write to content
                    line = line.replace("newPlayer", "onWhitelist");
                    content = content + line + System.lineSeparator();
                }
                line = br.readLine();
            }

            // Write new content to file
            FileWriter writer = new FileWriter(playerJoinTriesLocation);
            writer.write(content);

            writer.close();
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
                content = content + line + System.lineSeparator();;
                if (line.contains(playerName)) {
                    line = br.readLine();

                    // Reset tries and write to content
                    String cString = line;
                    cString = cString.replace("\"tries\":", "");
                    cString = cString.replaceAll("\"", "");
                    cString = cString.replaceAll("\\s+", "");
                    line = line.replace(cString, "0");
                    content = content + line + System.lineSeparator();

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

}
