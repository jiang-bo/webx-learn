/**
 * 
 */
package org.coderj.webx.demo.web.screen;

import com.alibaba.service.template.TemplateContext;
import com.alibaba.turbine.module.screen.TemplateScreen;
import com.alibaba.turbine.service.rundata.RunData;

/**
 * @author jiangbo
 * 
 */
public class Hello extends TemplateScreen {

	@Override
	public void execute(RunData rundata, TemplateContext context) {
		String name = (String) rundata.getParameters().get("name");
		name = "Mr." + name;
		context.put("name", name);
	}
}
