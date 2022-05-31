package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;

import java.io.*;

public class HandleFiles {

    //FIXME: FileLocation currently static. The Whitelist needs to be found in a dynamic way!
    private static String whitelistLocation = "whitelist.json";
    private static String playerJoinTriesLocation = "playerJoinTries.json";
    private static String whitelistedPlayerNames = "";
    private static String wLNW = "WhitelistNotFound";


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

    public static boolean putNameOnWhitelist(String playerName) {
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "whitelist add " + playerName);

        return true;
    }

    public static int handleJoinTries(String playerName) throws IOException {
        int triesRemaining = -1;

        try {
            FileReader fr = new FileReader(playerJoinTriesLocation);
            BufferedReader br = new BufferedReader(fr);
            String content = "";

            String line = br.readLine();
            while (line != null) {
                content = content + line + System.lineSeparator();

                if (line.contains(playerName)) {
                    line = br.readLine();

                    String cString = line.substring(17);
                    cString = cString.replaceAll("\"", "");
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
            File file = new File(playerJoinTriesLocation);
            BufferedWriter writer = new BufferedWriter(new FileWriter(playerJoinTriesLocation));
            writer.write("[]");

            writer.close();
            handleJoinTries(playerName);
        } catch (NumberFormatException numberFormatException) {
            // TODO: Handle no int found in numberOfTriesAlreadyDoneString
        } catch (IOException ioException) {
            // TODO: Handle IOException
        }

        return triesRemaining;
    }

}
