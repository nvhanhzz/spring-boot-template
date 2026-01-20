package sti.project.template.config.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerConfig {

    private LoggerConfig() {
    }

    public static final Logger EVENT = LoggerFactory.getLogger("sti.project.template.event");

    public static final Logger HTTP = LoggerFactory.getLogger("sti.project.template.http");
}
