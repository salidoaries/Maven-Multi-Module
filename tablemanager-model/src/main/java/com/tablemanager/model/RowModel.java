package com.tablemanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RowModel {
    private final List<CellModel> cells = new ArrayList<>();

    public void addCell(CellModel cell) {
        cells.add(cell);
    }

    public void addCells(List<CellModel> newCells) {
        cells.addAll(newCells);
    }

    public CellModel getCell(int index) {
        if (index < 0 || index >= cells.size()) {
            throw new IndexOutOfBoundsException("Invalid cell index");
        }
        return cells.get(index);
    }

    public List<CellModel> getCells() {
        return Collections.unmodifiableList(cells);
    }

    public int size() {
        return cells.size();
    }

    public void clearCells() {
        cells.clear();
    }

    // New method to sort cells by key
    public void sortCellsByKey(boolean ascending) {
        cells.sort((c1, c2) -> ascending
                ? c1.getKey().compareTo(c2.getKey())
                : c2.getKey().compareTo(c1.getKey()));
    }

    @Override
    public String toString() {
        return cells.toString();
    }
}
