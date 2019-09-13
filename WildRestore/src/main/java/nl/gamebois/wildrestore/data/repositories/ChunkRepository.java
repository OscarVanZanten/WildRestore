package nl.gamebois.wildrestore.data.repositories;

import java.sql.*;
import java.util.*;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import nl.gamebois.wildrestore.data.Database;
import nl.gamebois.wildrestore.world.models.*;

public class ChunkRepository extends BaseRepository<SerialChunk> {

    public ChunkRepository(Database db) {
        super(db);
    }

    public SerialChunk Get(int x, int z) {

        try {
            Statement chunkStatement = db.GetConnection().createStatement();
            String chunkSql = "select * from chunks where x = " + x + " and z =" + z;
            ResultSet chunkResult = chunkStatement.executeQuery(chunkSql);

            if (!chunkResult.next()) {
                return null;
            }
            int id = chunkResult.getInt("id");
            int chunkx = chunkResult.getInt("x");
            int chunkz = chunkResult.getInt("z");
            chunkStatement.close();

            Statement blocksStatement = db.GetConnection().createStatement();
            String blocksSql = "select * from blocks where chunkid = " + id;
            ResultSet blocksResult = blocksStatement.executeQuery(blocksSql);

            HashMap<Integer, SerialBlock> data = new HashMap<Integer, SerialBlock>();

            while (blocksResult.next()) {
                int blockx = blocksResult.getInt("x");
                int blockz = blocksResult.getInt("z");
                int blocky = blocksResult.getInt("y");
                int blockMaterial = blocksResult.getInt("material");
                byte blockSubtype = blocksResult.getByte("subtype");

                SerialBlock block = new SerialBlock(Material.getMaterial(blockMaterial), blockSubtype);
                int index = blockx + blockz * 16 + blocky * 16 * 255;
                data.put(index, block);
            }
            blocksResult.close();

            return new SerialChunk(data, chunkx, chunkz);
        } catch (SQLException e) {
            return null;
        }
    }

    public int GetId(int x, int z) {
        try {
            Statement chunkStatement = db.GetConnection().createStatement();
            String chunkSql = "select * from chunks where x = " + x + " and z =" + z;
            ResultSet chunkResult = chunkStatement.executeQuery(chunkSql);

            if (!chunkResult.next()) {
                return -1;
            }
            int id = chunkResult.getInt("id");
            chunkStatement.close();

            return id;
        } catch (SQLException e) {
            return -1;
        }
    }

    public SerialChunk Get(int id) {
        try {
            Statement chunkStatement = db.GetConnection().createStatement();
            String chunkSql = "select * from chunks where id = " + id;
            ResultSet chunkResult = chunkStatement.executeQuery(chunkSql);

            if (!chunkResult.next()) {
                return null;
            }

            int chunkx = chunkResult.getInt("x");
            int chunkz = chunkResult.getInt("z");
            chunkStatement.close();

            Statement blocksStatement = db.GetConnection().createStatement();
            String blocksSql = "select * from blocks where chunkid = " + id;
            ResultSet blocksResult = blocksStatement.executeQuery(blocksSql);

            HashMap<Integer, SerialBlock> data = new HashMap<Integer, SerialBlock>();

            while (blocksResult.next()) {
                int blockx = blocksResult.getInt("x");
                int blockz = blocksResult.getInt("z");
                int blocky = blocksResult.getInt("y");
                int blockMaterial = blocksResult.getInt("material");
                byte blockSubtype = blocksResult.getByte("subtype");

                SerialBlock block = new SerialBlock(Material.getMaterial(blockMaterial), blockSubtype);
                int index = blockx + blockz * 16 + blocky * 16 * 255;
                data.put(index, block);
            }
            blocksResult.close();

            return new SerialChunk(data, chunkx, chunkz);
        } catch (SQLException e) {
            return null;
        }
    }

    public SerialChunk[] GetAll() {
        try {
            Statement chunkStatement = db.GetConnection().createStatement();
            String chunkSql = "select * from chunks";
            ResultSet chunkResult = chunkStatement.executeQuery(chunkSql);

            HashMap<Integer, SerialChunk> chunks = new HashMap<Integer, SerialChunk>();
            while (chunkResult.next()) {
                int chunkId = chunkResult.getInt("id");
                int chunkx = chunkResult.getInt("x");
                int chunkz = chunkResult.getInt("z");

                chunks.put(chunkId, new SerialChunk(null, chunkx, chunkz));
            }
            chunkStatement.close();

            for (Map.Entry<Integer, SerialChunk> entry : chunks.entrySet()) {
                Statement blocksStatement = db.GetConnection().createStatement();
                String blocksSql = "select * from blocks where chunkid = " + entry.getKey();
                ResultSet blocksResult = blocksStatement.executeQuery(blocksSql);

                HashMap<Integer, SerialBlock> data = new HashMap<Integer, SerialBlock>();

                while (blocksResult.next()) {
                    int blockx = blocksResult.getInt("x");
                    int blockz = blocksResult.getInt("z");
                    int blocky = blocksResult.getInt("y");
                    int blockMaterial = blocksResult.getInt("material");
                    byte blockSubtype = blocksResult.getByte("subtype");

                    SerialBlock block = new SerialBlock(Material.getMaterial(blockMaterial), blockSubtype);
                    int index = blockx + blockz * 16 + blocky * 16 * 255;
                    data.put(index, block);
                }

                entry.getValue().SetData(data);
                blocksResult.close();

            }

            return chunks.values().toArray(new SerialChunk[chunks.values().size()]);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean Insert(SerialChunk t) {
        try {
            String chunkSql = "INSERT INTO chunks (x, z) VALUES (" + t.GetX() + "," + t.GetZ() + ");";
            Statement chunkStatement = db.GetConnection().createStatement();
            boolean success = chunkStatement.executeUpdate(chunkSql) == 1;

            if (!success) {
                return false;
            }
            chunkStatement.close();

            int id = GetId(t.GetX(), t.GetZ());

            String blocksSql = "INSERT INTO blocks (chunkid, x, y, z, material, subtype) VALUES ";

            for (Map.Entry<Integer, SerialBlock> entry : t.GetData().entrySet()) {

                if (entry.getValue().GetMaterial() != Material.AIR) {
                    int y = entry.getKey() / (16  * 16);
                    int z = (entry.getKey() - (y * 16* 16)) % 16 ;
                    int x = (entry.getKey() - (y * 16* 16)) / 16;

                    blocksSql += "(" + id + ", " + x + ", " + y + ", " + z + ", " + entry.getValue().GetMaterial().getId() + ", " + entry.getValue().GetData() + " ),";
                }
            }
            blocksSql = blocksSql.substring(0, blocksSql.length() - 1) + ';';

            Statement blockStatement = db.GetConnection().createStatement();
            blockStatement.executeUpdate(blocksSql);
            blockStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().info(e.toString());
            return false;
        }
    }

    public boolean Insert(List<SerialChunk> t) {
        for (SerialChunk chunk : t) {
            Insert(chunk);
        }
        return true;
    }

    public boolean Update(SerialChunk t) {
        return false;
    }

    public boolean Update(List<SerialChunk> t) {
        for (SerialChunk chunk : t) {
            Update(chunk);
        }
        return true;
    }

    public boolean Delete(SerialChunk t) {
        int id = GetId(t.GetX(), t.GetZ());

        try {
            Statement chunkStatement = db.GetConnection().createStatement();
            String chunksql = "DELETE FROM chunks WHERE id =" + id + ";";
            chunkStatement.executeUpdate(chunksql);
            chunkStatement.close();

            Statement blockStatement = db.GetConnection().createStatement();
            String blockssql = "DELETE FROM blocks WHERE chunkid =" + id + ";";
            blockStatement.executeUpdate(blockssql);
            blockStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean Delete(List<SerialChunk> t) {
        for (SerialChunk chunk : t) {
            Delete(chunk);
        }
        return true;
    }
}