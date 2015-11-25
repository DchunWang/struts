package org.apache.struts2.views.tiles;

import com.opensymphony.xwork2.ActionInvocation;
import freemarker.template.TemplateException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.result.ServletDispatcherResult;
import org.apache.struts2.portlet.PortletConstants;
import org.apache.struts2.portlet.context.PortletActionContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.portlet.context.PortletUtil;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Dedicated Tile result to be used in Portlet environment
 *
 * WW-2749
 */
public class PortletTilesResult extends ServletDispatcherResult {

    public static final String TILES_ACTION_NAME = "tilesDirect";

    public PortletTilesResult() {
        super();
    }

    public PortletTilesResult(String location) {
        super(location);
    }

    public void doExecute(String location, ActionInvocation invocation) throws Exception {
        if (PortletActionContext.getPhase().isAction() || PortletActionContext.getPhase().isEvent()) {
            executeActionResult(location, invocation);
        } else {
            executeRenderResult(location);
        }
    }

    protected void executeRenderResult(String location) throws TilesException {
        setLocation(location);

        TilesContainer container = PortletUtil.getContainer(PortletActionContext.getPortletContext());

        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();

        container.render(location, request, response);
    }

    protected void executeActionResult(String location, ActionInvocation invocation) {
        ActionResponse res = PortletActionContext.getActionResponse();

        res.setRenderParameter(PortletConstants.ACTION_PARAM, TILES_ACTION_NAME);

        Map<String, Object> sessionMap = invocation.getInvocationContext().getSession();
        sessionMap.put(PortletConstants.RENDER_DIRECT_LOCATION, location);

        res.setRenderParameter(PortletConstants.MODE_PARAM, PortletActionContext.getRequest().getPortletMode().toString());
    }

}
