package hello_world
import java.io.File
import java.lang.IndexOutOfBoundsException
import java.util.*
val scanner = Scanner(System.`in`)
val regex = "\\s+".toRegex()

fun argumentsChecking(inputArgs: Array<String>) {
    val validArguments = arrayOf("java SortingTool", "-dataType", "-sortingType", "natural", "byCount", "long", "word", "line", "-inputFile", "-outputFile")
    for (arg in inputArgs) {
        if (arg !in validArguments && inputArgs.indexOf(arg) != inputArgs.indexOf("-inputFile")+1 && inputArgs.indexOf(arg) != inputArgs.indexOf("-outputFile")+1) println("$arg is not a valid parameter. It will be skipped.")
    }
}

fun readWordOrNumber(): MutableList<String> {
    val firstLine = scanner.nextLine()
    val listOfElements = firstLine.split(regex).toMutableList()
    while (scanner.hasNext()) {
        val thisLine = scanner.nextLine()
        listOfElements.addAll(thisLine.split(regex).toMutableList())
    }
    return listOfElements
}
fun readLines(): MutableList<String> {
    val listOfElements = mutableListOf<String>()
    while (scanner.hasNext()) {
        listOfElements.add(scanner.nextLine())
    }
    return listOfElements
}
fun readLinesFromFile(inputFileName: String): MutableList<String> {
    val listOfElements: List<String> = File(inputFileName).readLines()
    return listOfElements.toMutableList()
}
fun readWordOrNumberFromFile(inputFileName: String): MutableList<String> {
    return File(inputFileName).readText().split(regex).toMutableList()
}

fun sortingNumbersNatural (inputFileName: String, outputFileName: String) {
    try {
        val listOfNumbers: MutableList<String> = if (inputFileName != "None") {
            readWordOrNumberFromFile(inputFileName)
        } else {
            readWordOrNumber()
        }
        listOfNumbers.map { it.toInt() }
        if (outputFileName == "None") {
            println("Total numbers: ${listOfNumbers.size}.")
            println("Sorted data: ${listOfNumbers.map { it.toInt() }.sorted().joinToString(separator = " ")}")
        }
        else {
            File(outputFileName).writeText("Total numbers: ${listOfNumbers.size}.\nSorted data: ${listOfNumbers.sorted().joinToString(separator = " ")}")
        }
    }
    catch(e: NumberFormatException) { println("All input elements mast be numbers. Your input has lines.") }
}
fun sortingWordsNatural(inputFileName: String, outputFileName: String) {
    val listOfWords: MutableList<String> = if (inputFileName != "None") {
        readWordOrNumberFromFile(inputFileName)
    } else {
        readWordOrNumber()
    }
    if (outputFileName == "None") {
        println("Total words: ${listOfWords.size}.")
        println("Sorted data: ${listOfWords.sorted().joinToString(separator = " ")}")
    }
    else {
        File(outputFileName).writeText("Total words: ${listOfWords.size}.\nSorted data: ${listOfWords.sorted().joinToString(separator = " ")}")
    }
}
fun sortingLinesNatural (inputFileName: String, outputFileName: String) {
    val listOfLines: MutableList<String> = if (inputFileName != "None") {
        readLinesFromFile(inputFileName)
    } else {
        readLines()
    }
    if (outputFileName == "None") {
        println("Total words: ${listOfLines.size}.")
        println("Sorted data:")
        println(listOfLines.sorted().joinToString(separator = "\n"))
    }
    else {
        File(outputFileName).writeText("Total words: ${listOfLines.size}.\nSorted data:\n${listOfLines.sorted().joinToString(separator = "\n")}")
    }
}

fun sortingByCount(inputMap: MutableList<String>, nameOfType: String, outputFileName: String) {
    val mapOfElements = mutableMapOf<String, Int>()
    for (element in inputMap) {
        if (mapOfElements.containsKey(element)) {
            mapOfElements[element] = mapOfElements[element]!! +1
        }
        else mapOfElements[element] = 1
    }
    if (outputFileName == "None") {
        println("Total $nameOfType: ${inputMap.size}.")
        for ((k, v) in mapOfElements.toList().sortedBy { it.second }.toMap()) {
            println("$k: $v time(s), ${v * 100 / inputMap.size}%")
        }
    }
    else {
        File(outputFileName).writeText("Total $nameOfType: ${inputMap.size}.\n")
        for ((k, v) in mapOfElements.toList().sortedBy { it.second }.toMap()) {
            File(outputFileName).appendText("$k: $v time(s), ${v * 100 / inputMap.size}%")
        }
    }
}

fun sortingNumbersByCount (inputFileName: String, outputFileName: String) {
    try {
        val listOfNumbers: MutableList<String> = if (inputFileName != "None") {
            readWordOrNumberFromFile(inputFileName)
        } else {
            readWordOrNumber()
        }
        listOfNumbers.map { it.toInt() }.toMutableList()
        sortingByCount(listOfNumbers.map { it.toInt() }.sorted().map { it.toString() } as MutableList<String>, "numbers", outputFileName)
    }
    catch (e: NumberFormatException) { println("All input elements mast be numbers. Your input has lines.") }
}
fun sortingWordsByCount(inputFileName: String, outputFileName: String) {
    val listOfWords: MutableList<String> = if (inputFileName != "None") {
        readWordOrNumberFromFile(inputFileName)
    } else {
        readWordOrNumber()
    }
    sortingByCount(listOfWords.sorted() as MutableList<String>, "words", outputFileName)
}
fun sortingLinesByCount(inputFileName: String, outputFileName: String) {
    val listOfLines: MutableList<String> = if (inputFileName != "None") {
        readLinesFromFile(inputFileName)
    } else {
        readLines()
    }
    sortingByCount(listOfLines.sorted() as MutableList<String>, "lines", outputFileName)
}

fun main(args: Array<String>) {

    argumentsChecking(args)

    val inputFileName = if ("-inputFile" in args) args[args.indexOf("-inputFile") + 1] else "None"
    val outputFileName = if ("-outputFile" in args) args[args.indexOf("-outputFile") + 1] else "None"

    if (!File(outputFileName).exists()) { File(outputFileName).createNewFile() }

    val typeOfData: String = try { if ("-dataType" in args) args[args.indexOf("-dataType") + 1] else "words"
    } catch (e: IndexOutOfBoundsException) { "Wrong" }
    val typeOfSorting: String = try { if ("-sortingType" in args) args[args.indexOf("-sortingType") + 1] else "natural"
    } catch (e: IndexOutOfBoundsException) { "Wrong" }

    if (typeOfData == "Wrong") println("No data type defined!")
    if (typeOfSorting == "Wrong") println("No sorting type defined!")

    if (typeOfSorting == "natural" && (typeOfData == "long")) sortingNumbersNatural(inputFileName, outputFileName)
    else if (typeOfSorting == "natural" && typeOfData == "word") sortingWordsNatural(inputFileName, outputFileName)
    else if (typeOfSorting == "natural" && typeOfData == "line") sortingLinesNatural(inputFileName, outputFileName)
    else if (typeOfSorting == "byCount" && typeOfData == "long") sortingNumbersByCount(inputFileName, outputFileName)
    else if (typeOfSorting == "byCount" && typeOfData == "word") sortingWordsByCount(inputFileName, outputFileName)
    else if (typeOfSorting == "byCount" && typeOfData == "line") sortingLinesByCount(inputFileName, outputFileName)
}
