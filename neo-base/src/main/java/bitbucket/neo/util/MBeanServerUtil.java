package bitbucket.neo.util;

import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * MBeanServerUtil
 *
 * @author arthas
 */
@Slf4j
public class MBeanServerUtil {

    private static final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    public static void registerMBean(Object obj, ObjectName objectName) {
        try {
            server.registerMBean(obj, objectName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }
}
