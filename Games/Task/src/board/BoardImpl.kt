package board

import board.Direction.*

open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    val cells: List<List<Cell>> = (1..width).map { i -> (1..width).map { j -> Cell(i, j) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (i < 1 || i > width) return null
        if (j < 1 || j > width) return null
        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        val cellOrNull = getCellOrNull(i, j)
        require(cellOrNull != null)
        return cellOrNull
    }

    override fun getAllCells(): List<Cell> {
        return cells.flatten()
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        return jRange.filter { it > 0 && it <= width }.map { getCell(i, it) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.filter { it > 0 && it <= width }.map { getCell(it, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(i - 1, j)
            DOWN -> getCellOrNull(i + 1, j)
            RIGHT -> getCellOrNull(i, j + 1)
            LEFT -> getCellOrNull(i, j - 1)
        }
    }

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)

class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {

    private val cellMap: MutableMap<Cell, T> = HashMap();

    override fun get(cell: Cell): T? {
        return cellMap[cell];
    }

    override fun set(cell: Cell, value: T?) {
        if (value == null) {
            cellMap.remove(cell)
        } else {
            cellMap[cell] = value
        }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return getAllCells().all { predicate.invoke(get(it)) }
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return getAllCells().any { predicate.invoke(get(it)) }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return getAllCells().find { predicate.invoke(get(it)) }
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return getAllCells().filter { predicate.invoke(get(it)) }
    }

}

