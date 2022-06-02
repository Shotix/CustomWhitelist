package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;

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
        int triesRemaining = 1;

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
        } catch (FileNotFoundException fileNotFoundException) {
            try {
                File file = new File(playerJoinTriesLocation);
                BufferedWriter writer = null;
                writer = new BufferedWriter(new FileWriter(playerJoinTriesLocation));
                writer.write("[]");

                writer.close();
                handleJoinTries(playerName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (NumberFormatException numberFormatException) {
            throw new RuntimeException(numberFormatException);
            // TODO: Handle NumberFormatException
        } catch (IOException ioException) {
            Bukkit.broadcastMessage("IOException");
            // TODO: Handle IOException
        }
        return triesRemaining;
    }

    public static void handlePutPlayerOnTries(String player) {
        String template = templateHandling(player);
        int line = findLineWritePlayerToJoinTries();
        writePlayerToJoinTries(template, line);
    }

    public static boolean isPlayerOnTries(String player) {
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            while (line != null) {
                if (line.contains(player)) return true;
                line = br.readLine();
            }

            br.close();
            fr.close();
        } catch (FileNotFoundException fileNotFoundException) {

        } catch (IOException e) {

        }
        return false;
    }

    public static String templateHandling(String name) {
        String template = "";

        try {
            FileReader fr = new FileReader(templateLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();

            while (line != null) {
                template = template + line + System.lineSeparator();
                line = br.readLine();
            }

            template = template.replace("putNameHere", name);

            br.close();
            fr.close();
        } catch (FileNotFoundException fileNotFoundException) {

        } catch (IOException ioException) {

        }
        return template;
    }

    public static int findLineWritePlayerToJoinTries() {
        int lineCounter = 0;
        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();

            while (line != null) {

                line = br.readLine();
                lineCounter++;
            }

            fr.close();
            br.close();
        } catch (FileNotFoundException ignore) {

        } catch (IOException e) {

        }
        return lineCounter;
    }

    public static void writePlayerToJoinTries(String template, int line) {
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

            if (line == 1) {
                newOutput = newOutput.replace("]", System.lineSeparator() + template) + "]";
            }
            else {
                newOutput = newOutput.replace("[", "[" + System.lineSeparator() + template);
            }

            writer.write(newOutput);
            writer.close();
            br.close();
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updatePlayerStatusToOnWhitelist(Player player) {

    }

    public static void updatePlayerStatusToVerified(Player player) {

    }
}
