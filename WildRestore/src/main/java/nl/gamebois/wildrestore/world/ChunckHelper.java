package nl.gamebois.wildrestore.world;

import java.util.*;  
import org.bukkit.*;
import org.bukkit.Chunk;
import org.bukkit.block.*;
import nl.gamebois.wildrestore.world.models.*;

public class ChunckHelper
{
    public static void SetChuncks(List<SerialChunk> chuncks, World world)
    {
        for(int i = 0; i < chuncks.size(); i++)
        {
            SerialChunk c = chuncks.get(i);

            Bukkit.getLogger().info("Processing chunck { x:" + c.GetX()+ ". z:" + c.GetZ()+" } " + i + "/" +  chuncks.size());
            SetChunck(c, world);
        }
    }
    
    public static void SetChunck(SerialChunk data, World world)
    {
        Chunk chunk = world.getChunkAt(data.GetX(), data.GetZ());

        for(int x =0; x < 16; x++)
        {
            for(int z =0; z < 16; z++)
            {
                for(int y =0; y < 255; y++)
                {
                    int index = x + z * 16 + y *16* 255;
                    SerialBlock m = data.GetData().get(index);
                    Block block = chunk.getBlock(  x, y, z);
                    block.setType(m.GetMaterial());
                    block.setData(m.GetData());
                    block.getState().update();
                }
            }
        }
    }

    public static List<SerialChunk> GetChuncks(World world)
    {
        List<SerialChunk> chuncks = new ArrayList<SerialChunk>();

        Chunk[] loadedChuncks = world.getLoadedChunks();

        for(int i = 0; i < loadedChuncks.length; i++)
        {
            Chunk c = loadedChuncks[i];

            Bukkit.getLogger().info("Processing chunck { x:" + c.getX()+ ". z:" + c.getZ()+" } " + i + "/" +  loadedChuncks.length);
            chuncks.add(GetChunck(c));
        }

        return chuncks;
    }

    public static SerialChunk GetChunck(Location location)
    {
        Chunk chunk = location.getWorld().getChunkAt(location);
        return GetChunck(chunk);
    }

    public static SerialChunk GetChunck( Chunk chunk)
    {
        HashMap<Integer, SerialBlock> data =  new HashMap<Integer, SerialBlock>();

        for(int y =0; y < 256; y++)
        {
            for(int x =0; x < 16; x++)
            {
                for(int z =0; z < 16; z++)
                {
                    Block block = chunk.getBlock(x, y, z);
                    int index = x + y * 16 + z * 16 * 256;
                    data.put( index , new SerialBlock(block.getType(), block.getData()));
                }
            }
        }
        return new SerialChunk(data, chunk.getX(), chunk.getZ());
    }

}