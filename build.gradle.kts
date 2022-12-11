import java.time.LocalDate
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
        test {
            java.srcDirs("test")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }
}

dependencies {
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.squareup.okhttp3:okhttp:4.9.3")
    }
}

fun getInput(day: Int): String {
    val client = OkHttpClient().newBuilder()
        .build()
    val request = Request.Builder()
        .url("https://adventofcode.com/2022/day/$day/input")
        .get()
        .addHeader("Cookie", "session=${properties["SESSION_COOKIE"]}" )
        .build()
    val response: Response = client.newCall(request).execute()
    if(response.code != 200){
        throw UncheckedIOException("Could not fetch challenge input. Status code: ${response.code}. Body: ${response.body?.string()}")
    }
    return response.body?.string() ?: ""
}

tasks.register("setup") {
    doLast {
        val day = getDay()
        val paddedDay = day.toString().padStart(2, '0')
        val newDirectory = File("src/day$paddedDay")
        if(newDirectory.exists()){
            throw IllegalArgumentException("There already exists a directory for day$paddedDay")
        }

        File("src/template").copyRecursively(newDirectory)
        val templateKotlinFile = File(newDirectory, "template.kt")
        val kotlinContents = templateKotlinFile.readText()
        File(newDirectory, "Day$paddedDay.kt").apply {
            createNewFile()
            writeText(kotlinContents
                .replace("template", "day$paddedDay")
                .replace("Template", "Day$paddedDay"))
        }
        templateKotlinFile.delete()

        val input = getInput(day)
        val inputFile = File(newDirectory, "input.txt")
        inputFile.writeText(input.trim())

    }
}

tasks.register("setTypes") {
    doLast {
        val day = getDay()
        val paddedDay = day.toString().padStart(2, '0')
        val dayKotlinFile = File("src/day$paddedDay", "Day$paddedDay.kt")
        if(!dayKotlinFile.exists()){
            throw IllegalArgumentException("No challenge file exists for day$paddedDay")
        }
        val inputType = properties["inputType"]?.toString() ?: "List<String>" // default to list of strings
        val returnType = properties["returnType"]?.toString() ?: "Long" // default to Long
        println("Setting input type to $inputType and return type to $returnType for day$paddedDay")
        val kotlinContents = dayKotlinFile.readText()
        dayKotlinFile.writeText(kotlinContents
            .replace(Regex("import (Input|Return)Type\\s+"), "")
            .replace("InputType", inputType)
            .replace("ReturnType", returnType)
        )
    }
}

tasks.register("getDayOfMonth")  {
    doLast {
        File(System.getenv("GITHUB_ENV"))
            .appendText("\nDAY_OF_MONTH=${getDay()}")
    }
}

fun getDay(): Int =  properties["DAY"]?.toString()?.toInt() ?: LocalDate.now().dayOfMonth