package cl;

import net.bytebuddy.agent.ByteBuddyAgent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class EhcAttachmentProvider implements ByteBuddyAgent.AttachmentProvider {
    private Class<?> vmClass = null;

    public EhcAttachmentProvider() {
        try {
            final Class<?> ehcAgentLoader = Class.forName("org.ehcache.sizeof.impl.AgentLoader");
            // ehcache found - get vm class from ehcache to work around
            // java.lang.UnsatisfiedLinkError: Native Library ... libattach.so already loaded in another classloader
            final Field vm_attach = ehcAgentLoader.getDeclaredField("VIRTUAL_MACHINE_ATTACH");
            vm_attach.setAccessible(true);
            vmClass = ((Method) vm_attach.get(null)).getDeclaringClass();
        } catch (Exception e) {
            log.error("ehcache is unavailable", e);
        }
    }

    @Override
    public Accessor attempt() {
        return new Accessor() {

            @Override
            public boolean isAvailable() {
                return vmClass != null;
            }

            @Override
            public Class<?> getVirtualMachineType() {
                return vmClass;
            }

            @Override
            public boolean isExternalAttachmentRequired() {
                return false;
            }

            @Override
            public ExternalAttachment getExternalAttachment() {
                return Unavailable.INSTANCE.getExternalAttachment();
            }
        };
    }
}
