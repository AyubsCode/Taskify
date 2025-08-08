import java.io.File
import java.util.Scanner
/**
 * Project Description: Create a command-line Kotlin app to manage a simple to-do list.
 * Users can add tasks, view the list of tasks, search for a task and remove a task.
 *
 * Date: August 4th, 2025
 *
 * Features:
 * 1. Add task to list
 * 2. Display all the tasks with indexes
 * 3. Search for a task by name
 * 4. Remove a task by name
 * 5. Exit the program
 *
 * Project Status: Complete
 */


data class Task(val title: String, val description: String)
//Global
const val fileName = "tasklist.csv"

fun isDuplicateTitle(title: String): Boolean {

    val file = File(fileName)
    if (!file.exists()) return false

    return file.readLines().any { line ->
        val parts = line.split("::")
        parts.getOrNull(0)?.equals(title, ignoreCase = true) == true
    }
}

/**
 * Purpose: Create a task and store it in a list ( later into a file for persistence)
 */
fun createTask() {

    while (true) {
        print("Enter a title(15 character max): ")
        val title = readln().trim()
        print("Provide a description(25 characters max): ")
        val desc = readln().trim()

        if (title.length !in 1..15 && desc.length !in 1..25) {
            println("[!] Invalid input. Try again.")
            continue
        }

        if (isDuplicateTitle(title)) {
            println("A task with the title '${title}' exists. Try a different name.")
            continue
        }
        val task = Task(title,desc)
        File(fileName).appendText("${task.title}::${task.description}\n")
        println("[+] Task Successfully Added.")
        break
    }
}

/**
 * Purpose: Display the contents from text file
 */
fun displayTasks(){

    val file = File(fileName)
    if (!file.exists() || file.readLines().isEmpty()){
        println("=======\nYou don't have any tasks yet. Create one to see it listed here.\n=======")
        return
    }
    println("\nYour Tasks: \n=======")
    file.readLines().forEach { line ->
        val parts = line.split("::")
        if (parts.size == 2){
            println(" ${parts[0]}")
        }
    }
}

/**
 * Purpose: Find the task name and remove it from text file
 */
fun deleteTask(){

    print("Provide the name of the task to remove: ")
    val title = readln().trim()

    val file = File(fileName)
    val task = file.readLines().mapNotNull { line ->
        val parts = line.split("::")
        if (parts.size == 2) Task(parts[0],parts[1]) else null
    }

    val updatedTasks = task.filterNot { it.title.equals(title,true) }
    if (updatedTasks.size == task.size){
       println("=======\n[!] Task not found.\n=======")
    }
    else {
        file.writeText("") //clear file
        updatedTasks.forEach { file.appendText("${it.title}::${it.description}\n") }
        println("[+] Task was deleted successfully.")
    }
}

/**
 * Purpose: Find the task by name and display both the label and description
 */
fun searchTask(){

    print("Provide the name of the task to search: ")
    val label = readln().trim()

    if (label.isBlank()){
        println("=======\n[!] Input can't be empty.\n=======")
        return
    }
    val found = File(fileName).readLines().mapNotNull { line ->
        val parts = line.split("::")
        if (parts.size == 2) Task(parts[0],parts[1]) else null
    }.filter{ it.title.contains(label,true)}

    if (found.isNotEmpty()){
        println("Match found: ")
        found.forEach {
            println("============\nTitle:${it.title}\nDescription:${it.description}\n============") }
    }
    else{
        println("=======\nNo tasks found.\n=======")
    }
}

fun main() {

    val scanner = Scanner(System.`in`)
    var running = true

    while (running) {
        //Display menu options
        println("\nChoose one of the options: ")
        MenuOption.entries.forEachIndexed { index, option ->
            println("${index + 1}. ${option.name}")
        }

        //Read user input
        print("\nEnter your choice (1-${MenuOption.entries.size}): ")
        val choice = scanner.nextLine().toIntOrNull()
        val selectedOption = if (choice != null && choice in 1..MenuOption.entries.size) {
            MenuOption.entries[choice - 1]
        } else {
            println("[!] Invalid choice. Please try again.")
            continue
        }

        //Handle user choice
        when (selectedOption) {
            MenuOption.AddTask -> createTask()
            MenuOption.DisplayTask -> displayTasks()
            MenuOption.DeleteTask -> deleteTask()
            MenuOption.SearchTask -> searchTask()
            MenuOption.Exit -> {
                println("\nExiting program.")
                running = false
            }
        }
    }
}





