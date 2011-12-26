/**
 * 
 */
package org.coderj.webx.demo.web.action;

import com.alibaba.webx.ActionResult;
import com.alibaba.webx.action.Parameter;

/**
 * @author jiangbo
 * 
 */
public class HelloAction {

	public ActionResult sayHi(@Parameter(name = "name") String name) {
		ActionResult ar = ActionResult.create(this);
		name = "Mr." + name;
		ar.putInContext("name", name);
		return ar;
	}
}
