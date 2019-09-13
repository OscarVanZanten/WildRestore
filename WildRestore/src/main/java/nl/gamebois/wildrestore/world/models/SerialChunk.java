package nl.gamebois.wildrestore.world.models;

import java.util.*;  

public class SerialChunk
{
    private HashMap<Integer, SerialBlock> data;
    private int x;
    private int z;

    public SerialChunk(HashMap<Integer, SerialBlock> data,int x,int z)
    {
        this.data = data;
        this.x = x;
        this.z = z;
    }

    public HashMap<Integer, SerialBlock> GetData()
    {
        return this.data;
    }

    public void SetData(HashMap<Integer, SerialBlock>  data)
    {
       this.data = data;
    }

    public int GetX()
    {
        return this.x;
    }

    public int GetZ()
    {
        return this.z;
    }
}