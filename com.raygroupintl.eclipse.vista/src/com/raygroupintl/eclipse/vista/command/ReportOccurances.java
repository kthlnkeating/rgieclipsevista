package com.raygroupintl.eclipse.vista.command;

import java.util.List;

import com.raygroupintl.m.tool.ParseTreeSupply;
import com.raygroupintl.m.tool.ResultsByRoutine;
import com.raygroupintl.m.tool.routine.MRoutineToolInput;
import com.raygroupintl.m.tool.routine.occurance.Occurance;
import com.raygroupintl.m.tool.routine.occurance.OccuranceTool;
import com.raygroupintl.m.tool.routine.occurance.OccuranceToolParams;

public class ReportOccurances extends MToolsCommand {
	@Override
	protected ResultsByRoutine<Occurance, List<Occurance>> getResult(ParseTreeSupply pts, List<String> selectedFileNames) {
		OccuranceToolParams p = new OccuranceToolParams(pts);	
		OccuranceTool tool = new OccuranceTool(p);
		MRoutineToolInput input = new MRoutineToolInput();
		input.addRoutines(selectedFileNames);
		ResultsByRoutine<Occurance, List<Occurance>> result = tool.getResult(input);
		return result;
	}
}
