package leekscript.compiler.expression;

import leekscript.compiler.IAWord;
import leekscript.compiler.JavaWriter;
import leekscript.compiler.WordCompiler;
import leekscript.compiler.bloc.MainLeekBlock;
import leekscript.common.Error;
import leekscript.common.Type;

public class LeekTernaire extends LeekExpression {

	private AbstractExpression mCondition;

	private int mOperator = 0;
	private Type type = Type.ANY;

	public LeekTernaire() {
		mCondition = null;
		mExpression1 = null;
		mExpression2 = null;
	}

	@Override
	public boolean needOperator() {
		if(mCondition != null && mOperator == 0){
			if(mCondition.getNature() == EXPRESSION){
				return ((LeekExpression) mCondition).needOperator();
			}
			return true;
		}
		if(mExpression1 != null && mOperator == 1){
			if(mExpression1.getNature() == EXPRESSION){
				return ((LeekExpression) mExpression1).needOperator();
			}
			return true;
		}
		if(mExpression2 != null && mOperator == 2){
			if(mExpression2.getNature() == EXPRESSION){
				return ((LeekExpression) mExpression2).needOperator();
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasTernaire() {
		return true;
	}

	@Override
	public int getNature() {
		return EXPRESSION;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String getString() {
		String retour = "";
		retour += mCondition == null ? "null" : mCondition.getString();
		retour += " ? ";
		retour += mExpression1 == null ? "null" : mExpression1.getString();
		retour += " : ";
		retour += mExpression2 == null ? "null" : mExpression2.getString();
		return retour;
	}

	@Override
	public void writeJavaCode(MainLeekBlock mainblock, JavaWriter writer) {
		// if(mCondition instanceof LeekExpression) mCondition = ((LeekExpression) mCondition).getAbstractExpression();
		// if(mExpression1 instanceof LeekExpression) mExpression1 = ((LeekExpression) mExpression1).getAbstractExpression();
		// if(mExpression2 instanceof LeekExpression) mExpression2 = ((LeekExpression) mExpression2).getAbstractExpression();
		if (!complete()) writer.addCode("/* " + getString() + " */");
		else {
			var branch_ops = mExpression1.operations != mExpression2.operations;
			writer.getBoolean(mainblock, mCondition);
			writer.addCode(" ? ");
			if (mExpression1.getOperations() > 0 && branch_ops) writer.addCode("ops(");
			else writer.addCode("(");
			mExpression1.writeJavaCode(mainblock, writer);
			if (mExpression1.getOperations() > 0 && branch_ops) writer.addCode(", " + mExpression1.getOperations() + ")");
			else writer.addCode(")");
			writer.addCode(" : ");
			if (mExpression2.getOperations() > 0 && branch_ops) writer.addCode("ops(");
			else writer.addCode("(");
			mExpression2.writeJavaCode(mainblock, writer);
			if (mExpression2.getOperations() > 0 && branch_ops) writer.addCode(", " + mExpression2.getOperations() + ")");
			else writer.addCode(")");
		}
	}

	@Override
	public boolean validExpression(WordCompiler compiler, MainLeekBlock mainblock) throws LeekExpressionException {
		if(!complete()) throw new LeekExpressionException(this, Error.UNCOMPLETE_EXPRESSION);
		if(!(mCondition.validExpression(compiler, mainblock) && mExpression1.validExpression(compiler, mainblock) && mExpression2.validExpression(compiler, mainblock))) throw new LeekExpressionException(this, Error.UNCOMPLETE_EXPRESSION);
		return true;
	}

	@Override
	public void addExpression(AbstractExpression expression) {
		if(mCondition == null) mCondition = expression;
		else if(mCondition.getNature() == EXPRESSION && !((LeekExpression) mCondition).complete()){
			((LeekExpression) mCondition).addExpression(expression);
		}
		else if(mOperator == 1){
			if(mExpression1 == null) mExpression1 = expression;
			else ((LeekExpression) mExpression1).addExpression(expression);
		}
		else if(mOperator == 2){
			if(mExpression2 == null) mExpression2 = expression;
			else ((LeekExpression) mExpression2).addExpression(expression);
		}
	}

	@Override
	public void addUnarySuffix(int suffix, IAWord token) {
		//On doit ajouter ce suffix au dernier ??l??ment ajout??
		if(mCondition != null && mExpression1 == null && mExpression2 == null){
			if(mCondition.getNature() == EXPRESSION) ((LeekExpression) mCondition).addUnarySuffix(suffix, token);
			else{
				//On doit ajouter ?? l'??l??ment mExpression1
				LeekExpression exp = new LeekExpression();
				exp.setParent(this);
				exp.setExpression1(new LeekNull());
				exp.setOperator(suffix, token);
				exp.setExpression2(mCondition);
				mCondition = exp;
			}
		}
		else if(mExpression1 != null && mExpression2 == null){
			if(mExpression1.getNature() == EXPRESSION) ((LeekExpression) mExpression1).addUnarySuffix(suffix, token);
			else{
				//On doit ajouter ?? l'??l??ment mExpression1
				LeekExpression exp = new LeekExpression();
				exp.setParent(this);
				exp.setExpression1(new LeekNull());
				exp.setOperator(suffix, token);
				exp.setExpression2(mExpression1);
				mExpression1 = exp;
			}
		}
		else if(mExpression2 != null){
			if(mExpression2.getNature() == EXPRESSION) ((LeekExpression) mExpression2).addUnarySuffix(suffix, token);
			else{
				//On doit ajouter ?? l'??l??ment mExpression2
				LeekExpression exp = new LeekExpression();
				exp.setParent(this);
				exp.setExpression1(new LeekNull());
				exp.setOperator(suffix, token);
				exp.setExpression2(mExpression2);
				mExpression2 = exp;
			}
		}
	}

	@Override
	public boolean complete(int operator) {
		if(!complete()) return false;
		if(operator >= Operators.getPriority(Operators.DOUBLE_POINT)) return false;
		return true;
	}

	@Override
	public boolean complete() {
		if(!super.complete()) return false;
		if(mCondition == null) return false;
		if(mCondition.getNature() == EXPRESSION && !((LeekExpression) mCondition).complete()) return false;
		return true;
	}

	@Override
	public void addOperator(int operator, IAWord token) {
		//On doit trouver ?? quel endroit de l'arborescence on doit placer l'op??rateur
		if(mOperator == 0 && operator == Operators.TERNAIRE){
			mOperator = 1;
		}
		else if(mExpression1.getNature() == EXPRESSION && !((LeekExpression) mExpression1).complete()) ((LeekExpression) mExpression1).addOperator(operator, token);
		else if(mOperator == 1 && operator == Operators.DOUBLE_POINT){
			mOperator = 2;
		}
		else{
			if(mOperator == 0){
				if(mCondition.getNature() == EXPRESSION) ((LeekExpression) mCondition).addOperator(operator, token);
				else{
					LeekExpression new_e = new LeekExpression();
					new_e.setParent(this);
					new_e.setExpression1(mCondition);
					new_e.setOperator(operator, token);
					mCondition = new_e;
				}
			}
			else if(mOperator == 1){
				if(mExpression1.getNature() == EXPRESSION) ((LeekExpression) mExpression1).addOperator(operator, token);
				else{
					if(operator == Operators.TERNAIRE){
						LeekTernaire new_e = new LeekTernaire();
						new_e.setParent(this);
						new_e.addExpression(mExpression1);
						new_e.addOperator(operator, token);
						mExpression1 = new_e;
					}
					else{
						LeekExpression new_e = new LeekExpression();
						new_e.setParent(this);
						new_e.setExpression1(mExpression1);
						new_e.setOperator(operator, token);
						mExpression1 = new_e;
					}
				}
			}
			else{
				if(mExpression2.getNature() == EXPRESSION) ((LeekExpression) mExpression2).addOperator(operator, token);
				else{
					if(operator == Operators.TERNAIRE){
						LeekTernaire new_e = new LeekTernaire();
						new_e.setParent(this);
						new_e.addExpression(mExpression2);
						new_e.addOperator(operator, token);
						mExpression2 = new_e;
					}
					else{
						LeekExpression new_e = new LeekExpression();
						new_e.setParent(this);
						new_e.setExpression1(mExpression2);
						new_e.setOperator(operator, token);
						mExpression2 = new_e;
					}
				}
			}
		}
	}

	@Override
	public void analyze(WordCompiler compiler) {
		if (mCondition != null) {
			mCondition.analyze(compiler);
			operations = 1 + mCondition.getOperations();
		}
		mExpression1.analyze(compiler);
		mExpression2.analyze(compiler);

		if (mExpression1.operations == mExpression2.operations) {
			operations += mExpression1.operations;
		}

		if (mExpression1 != null && mExpression2 != null && mExpression1.getType() == mExpression2.getType()) {
			type = mExpression1.getType();
		}
	}
}
