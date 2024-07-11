package de.halfbit.logger

public enum class LoggableLevel(
    public val weight: Int,
) {
    /** For sending all messages to registered sinks. */
    Everything(LogLevel.Debug.weight),

    /** For sending `LogLevel.Info`, `LogLevel.Warning` and `LogLevel.Error` messages to registered sinks. */
    InfoAndSevere(LogLevel.Info.weight),

    /** For sending `LogLevel.Warning` and `LogLevel.Error` messages to registered sinks. */
    WarningsAndSevere(LogLevel.Warning.weight),

    /** For sending `LogLevel.Error` messages only to registered sinks. */
    ErrorsOnly(LogLevel.Error.weight),

    /** For sending no messages to registered sinks. */
    Nothing(LogLevel.Error.weight + 1),
}
