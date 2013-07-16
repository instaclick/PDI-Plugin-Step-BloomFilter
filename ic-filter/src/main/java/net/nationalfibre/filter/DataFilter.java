package net.nationalfibre.filter;

public interface DataFilter {

    public boolean add(Data click);

    public boolean contains(Data click);

    public void flush();
}