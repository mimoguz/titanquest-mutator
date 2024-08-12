package io.github.mimoguz.tqmutator;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class TitanQuestCharacterFile {

    static final String MARKER_HEADER_VERSION = "headerVersion";
    static final String MARKER_PLAYER_LEVEL = "playerLevel";
    static final String MARKER_PLAYER_NAME = "myPlayerName";
    static final String MARKER_MONEY = "money";
    static final String MARKER_ATTRIBUTE_POINTS = "modifierPoints";
    static final String MARKER_SKILLPOINTS = "skillPoints";
    static final String MARKER_STATS_KILLS = "numberOfKills";
    static final String MARKER_STATS_DEATHS = "numberOfDeaths";
    static final List<String> MARKER_LIST = List.of(
            MARKER_HEADER_VERSION,
            MARKER_PLAYER_LEVEL,
            MARKER_PLAYER_NAME,
            MARKER_MONEY,
            MARKER_ATTRIBUTE_POINTS,
            MARKER_SKILLPOINTS,
            MARKER_STATS_KILLS,
            MARKER_STATS_DEATHS);

    private final File file;
    private final Map<String, Integer> markers;
    private final int headerVersion;
    private final String oldName;
    private String characterName;
    private int level;
    private int money;
    private int attributePoints;
    private int skillPoints;
    private int numberOfKills;
    private int numberOfDeaths;
    private Logger logger;

    public TitanQuestCharacterFile(
            File file,
            Map<String, Integer> markers,
            int headerVersion,
            String characterName,
            int level,
            int money,
            int attributePoints,
            int skillPoints,
            int numberOfKills,
            int numberOfDeaths,
            Logger logger) {
        this.file = file;
        this.markers = markers;
        this.headerVersion = headerVersion;
        this.characterName = characterName;
        this.oldName = characterName;
        this.level = level;
        this.money = money;
        this.attributePoints = attributePoints;
        this.skillPoints = skillPoints;
        this.numberOfKills = numberOfKills;
        this.numberOfDeaths = numberOfDeaths;
        this.logger = logger;
    }

    static TitanQuestCharacterFile load(File file, Logger logger) throws IOException {
        final var bytes = Files.readAllBytes(file.toPath());
        final var markers = findMarkers(bytes, MARKER_LIST, logger);
        final var buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        final var timeStart = System.currentTimeMillis();
        final var headerVersion = readByte(buffer, markers.get(MARKER_HEADER_VERSION));
        final var playerLevel = readByte(buffer, markers.get(MARKER_PLAYER_LEVEL));
        final var playerName = readUTF16(buffer, markers.get(MARKER_PLAYER_NAME));
        final var money = readInt(buffer, markers.get(MARKER_MONEY));
        final var attributePoints = readInt(buffer, markers.get(MARKER_ATTRIBUTE_POINTS));
        final var skillPoints = readInt(buffer, markers.get(MARKER_SKILLPOINTS));
        final var numberOfKills = readInt(buffer, markers.get(MARKER_STATS_KILLS));
        final var numberOfDeaths = readInt(buffer, markers.get(MARKER_STATS_DEATHS));
        final var duration = System.currentTimeMillis() - timeStart;
        logger.info("LOADING character file was successful, took " + duration + "ms");

        return new TitanQuestCharacterFile(
                file,
                markers,
                headerVersion,
                playerName,
                playerLevel,
                money,
                attributePoints,
                skillPoints,
                numberOfKills,
                numberOfDeaths,
                logger);
    }

    private static String readUTF16(ByteBuffer buffer, int offset) {
        buffer.position(offset);
        final var length = buffer.getInt() * 2; // Each character is two bytes
        final var bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_16LE);
    }

    private static int readInt(ByteBuffer buffer, int offset) {
        return buffer.getInt(offset);
    }

    private static byte readByte(ByteBuffer buffer, int offset) {
        return buffer.get(offset);
    }

    private static Map<String, Integer> findMarkers(byte[] bytes, List<String> markers, Logger logger) {
        final var markerMap = new HashMap<String, Integer>();
        for (final var marker : markers) {
            final var markerBytes = marker.getBytes();
            final var index = ByteUtils.indexOf(bytes, markerBytes);
            if (index == -1) {
                logger.warning("Expected marker" + marker + " could not be found in savefile");
                continue;
            }
            final var offset = index + markerBytes.length;
            markerMap.put(marker, offset);
        }
        return markerMap;
    }

    private static void writeInt(ByteBuffer buffer, int offset, int value) {
        buffer.putInt(offset, value);
    }

    public File getFile() {
        return file;
    }

    public Map<String, Integer> getMarkers() {
        return markers;
    }

    public int getHeaderVersion() {
        return headerVersion;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getOldName() {
        return oldName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getAttributePoints() {
        return attributePoints;
    }

    public void setAttributePoints(int attributePoints) {
        this.attributePoints = attributePoints;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    public int getNumberOfKills() {
        return numberOfKills;
    }

    public void setNumberOfKills(int numberOfKills) {
        this.numberOfKills = numberOfKills;
    }

    public int getNumberOfDeaths() {
        return numberOfDeaths;
    }

    public void setNumberOfDeaths(int numberOfDeaths) {
        this.numberOfDeaths = numberOfDeaths;
    }

    public TitanQuestCharacterFile save() throws IOException {
        final var timeStart = System.currentTimeMillis();
        final var bytes = Files.readAllBytes(file.toPath());
        var buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        if (markers.containsKey(MARKER_PLAYER_LEVEL)) {
            writeInt(buffer, markers.get(MARKER_PLAYER_LEVEL), level);
        }
        if (markers.containsKey(MARKER_MONEY)) {
            writeInt(buffer, markers.get(MARKER_MONEY), money);
        }
        if (markers.containsKey(MARKER_ATTRIBUTE_POINTS)) {
            writeInt(buffer, markers.get(MARKER_ATTRIBUTE_POINTS), attributePoints);
        }
        if (markers.containsKey(MARKER_SKILLPOINTS)) {
            writeInt(buffer, markers.get(MARKER_SKILLPOINTS), skillPoints);
        }
        if (markers.containsKey(MARKER_STATS_KILLS)) {
            writeInt(buffer, markers.get(MARKER_STATS_KILLS), numberOfKills);
        }
        if (markers.containsKey(MARKER_STATS_DEATHS)) {
            writeInt(buffer, markers.get(MARKER_STATS_DEATHS), numberOfDeaths);
        }

        // write name last, as this changes file length and thus invalidates all markers
        final var nameIsDirty = !oldName.equals(characterName);
        if (nameIsDirty) {
            final var nameOffset = markers.get(MARKER_PLAYER_NAME);
            final var currentNameLength = readInt(buffer, nameOffset) * 2;
            final var allBytes = buffer.array();
            final var bytesPart1 = new byte[nameOffset];
            final var part2Length = allBytes.length - nameOffset - currentNameLength - 4;
            final var bytesPart2 = new byte[part2Length];
            System.arraycopy(allBytes, 0, bytesPart1, 0, nameOffset);
            System.arraycopy(allBytes, nameOffset + 4 + currentNameLength, bytesPart2, 0, part2Length);
            final var newName = characterName;
            final var newNameArray = new byte[4 + newName.length() * 2];
            final var newNameBuffer = ByteBuffer.wrap(newNameArray);
            newNameBuffer.order(ByteOrder.LITTLE_ENDIAN);
            newNameBuffer.putInt(newName.length());
            newNameBuffer.put(newName.getBytes(StandardCharsets.UTF_16LE));

            final var result = new byte[bytesPart1.length + newNameArray.length + bytesPart2.length];
            System.arraycopy(bytesPart1, 0, result, 0, bytesPart1.length);
            System.arraycopy(newNameArray, 0, result, bytesPart1.length, newNameArray.length);
            System.arraycopy(bytesPart2, 0, result, bytesPart1.length + newNameArray.length, bytesPart2.length);
            buffer = ByteBuffer.wrap(result);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        }

        Files.write(file.toPath(), buffer.array());

        if (nameIsDirty) {
            final var parent = file.getParentFile();
            if (parent != null && parent.isDirectory() && parent.getName().equals("_" + oldName)) {
                final var newPath = Paths.get(parent.getParentFile().getPath(), "_" + characterName);
                final var newDir = new File(newPath.toString());
                if (!parent.renameTo(newDir)) {
                    logger.warning("Could not rename directory to " + newDir.getName());
                }
                final var newFilePath = Paths.get(newDir.getPath(), file.getName());
                logger.info("Path changed, reloading character file from " + newDir.getName());
                return load(new File(newFilePath.toString()), logger);
            } else {
                logger.info("Parent directory name does not match, standalone savefile?"
                        + "Skip renaming of parent directory");
            }
        }

        final var duration = System.currentTimeMillis() - timeStart;
        logger.info("SAVING character file was successful, took " + duration + "ms");
        return this;
    }

}
