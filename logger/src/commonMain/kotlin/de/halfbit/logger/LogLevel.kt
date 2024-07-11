/** Copyright 2024 Halfbit GmbH, Sergej Shafarenka */
package de.halfbit.logger

public enum class LogLevel(
    public val weight: Int,
    public val long: String,
    public val short: Char,
) {
    Debug(10, "DEBUG", 'D'),
    Info(20, "INFO", 'I'),
    Warning(30, "WARN", 'W'),
    Error(40, "ERROR", 'E');
}
