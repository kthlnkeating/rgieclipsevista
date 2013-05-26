package com.raygroupintl.eclipse.vista.command;

import java.util.List;

import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ToolResult;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.RoutineToolParams;
import com.raygroupintl.m.tool.routine.error.ErrorTool;

public class ReportErrors extends MToolsCommand {
	@Override
	protected ToolResult getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		RoutineToolParams p = new RoutineToolParams(pts);
		ErrorTool tool = new ErrorTool(p);
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutines(selectedFileNames);
		ToolResult result = tool.getResult(input);
		return result;
	}
}
