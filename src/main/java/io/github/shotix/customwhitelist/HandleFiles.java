package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class HandleFiles {

    private final static String whitelistLocation = "whitelist.json";
    private final static String playerJoinTriesLocation = "playerJoinTries.json";
    private final static String passwordLocation = "customWhitelistPassword.txt";


    public static boolean isPlayerOnWhitelist(String playerName) {
        try {
            FileReader fr = new FileReader(whitelistLocation);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                if (line.contains(playerName)) {
                    br.close();
                    fr.close();
                    return true;
                }
                line = br.readLine();
            }
            br.close();
            fr.close();
            return false;
        } catch (IOException fnf) {
            throw new RuntimeException(fnf);
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

            FileWriter writer = new FileWriter(playerJoinTriesLocation);
            writer.write(content);
            writer.close();
            br.close();
            fr.close();
        } catch (IOException fileNotFoundException) {
            throw  new RuntimeException(fileNotFoundException);
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
                content = content + line + System.lineSeparator();
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
                content = content + line + System.lineSeparator();
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

                    // Reset tries and write to content
                    String cString = line;
                    cString = cString.replace("\"tries\":", "");
                    cString = cString.replaceAll("\"", "");
                    cString = cString.replaceAll("\\s+", "");
                    line = line.replace(cString, "0");
                    content = content + line + System.lineSeparator();

                    line = br.readLine();

                    // Change status and write to content
                    line = line.replace("newPlayer", "banned");
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

                    // Reset tries and write to content
                    String cString = line;
                    cString = cString.replace("\"tries\":", "");
                    cString = cString.replaceAll("\"", "");
                    cString = cString.replaceAll("\\s+", "");
                    line = line.replace(cString, "0");
                    content = content + line + System.lineSeparator();

                    line = br.readLine();

                    // Change status and write to content
                    line = line.replace("banned", "newPlayer");
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

    public static void setPassword(String password) {
        try{
            FileWriter writer = new FileWriter(passwordLocation);
            writer.write(password);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String getPassword() {
        String password = "join";

        try {
            FileReader fr = new FileReader(passwordLocation);
            BufferedReader br = new BufferedReader(fr);

            password = br.readLine();

            br.close();
            fr.close();
        } catch (FileNotFoundException fileNotFoundException) {
            setPassword(password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return password;
    }
}
