package nl.gamebois.wildrestore.world.models;

import org.bukkit.*;

public class SerialBlock

{
    private Material material;
    private byte data;

    public SerialBlock(Material material, byte data)
    {
        this.material = material;
        this.data  = data;
    }

    public Material GetMaterial()
    {
        return this.material;
    }

    public byte GetData()
    {
        return this.data;
    }
}