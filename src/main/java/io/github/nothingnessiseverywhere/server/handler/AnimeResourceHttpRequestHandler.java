package io.github.nothingnessiseverywhere.server.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Component
public class AnimeResourceHttpRequestHandler extends ResourceHttpRequestHandler {
    public static final String REQUEST_ATTR_FILE_PATH = "filePath"; // 请求属性名

    @Override
    protected Resource getResource(HttpServletRequest request) {
        // 从请求属性中获取文件路径
        String filePath = (String) request.getAttribute(REQUEST_ATTR_FILE_PATH);
        if (filePath == null) {
            return null; // 路径不存在时返回null，父类会处理404
        }
        return new FileSystemResource(filePath); // 创建文件资源
    }
}
