package games.gameOfFifteen

import board.Cell
import board.Direction
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
    GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board = createGameBoard<Int?>(4)

    private var emptyCell = Cell(4,4)

    override fun initialize() {
        val permIter = initializer.initialPermutation.iterator()
        val cellIter = board.getAllCells().iterator()
        while (permIter.hasNext()) {
            board[cellIter.next()] = permIter.next()
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val allCells: List<Cell> = board.getAllCells();
        return IntRange(1, 15).all { board[allCells[it-1]] == it }
    }

    override fun processMove(direction: Direction) {
        with(board) {
            val nextEmptyCell = emptyCell.getNeighbour(direction.reversed()) ?: return
            this[emptyCell] = this[nextEmptyCell]
            this[nextEmptyCell] = null
            emptyCell = nextEmptyCell
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

}

