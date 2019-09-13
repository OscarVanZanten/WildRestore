package nl.gamebois.wildrestore.data.repositories;

import java.util.*;  
import nl.gamebois.wildrestore.data.Database;

public abstract class BaseRepository<T>
{
    protected Database db;

    public BaseRepository(Database db)
    {
        this.db = db;
    }

    public abstract T Get(int id);
    public abstract T[] GetAll();
    public abstract boolean Insert(T t);
    public abstract boolean Insert(List<T> t);
    public abstract boolean Update(T t);
    public abstract boolean Update(List<T> t);
    public abstract boolean Delete(T t);
    public abstract boolean Delete(List<T> t);
}