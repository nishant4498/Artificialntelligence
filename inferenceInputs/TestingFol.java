

import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;

public class TestingFol {

	public static String path = "F:\\workspace\\java\\AIAssignment1\\inferenceInputs\\";
	
	@Test
	
	
	public void AmericanWesttest() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path + "americanwestinput.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		Assert.assertTrue(result[6]==true);
		Assert.assertTrue(result[7]==true);
		Assert.assertTrue(result[8]==true);
	}
	@Test
	public void InputTestcaseTest() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"inputcase.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==false);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		
	}
	@Test
	public void modifiedInputTestcaseTest() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"modifedinputcase.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==false);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==false);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		
	}
	@Test
	public void skisInputTestcaseTest() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"skicase.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==false);
		Assert.assertTrue(result[3]==false);
		Assert.assertTrue(result[4]==true);
		Assert.assertTrue(result[5]==true);
		Assert.assertTrue(result[6]==true);
		Assert.assertTrue(result[7]==true);
		Assert.assertTrue(result[8]==true);
		Assert.assertTrue(result[9]==true);
		Assert.assertTrue(result[10]==true);
		Assert.assertTrue(result[11]==true);
		Assert.assertTrue(result[12]==true);
		Assert.assertTrue(result[13]==true);
		Assert.assertTrue(result[14]==false);
		Assert.assertTrue(result[15]==true);
		}
	@Test
	public void InputTestcaseTestNegated() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"negatedinputCase.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==false);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		
	}
	
	@Test
	public void modifiedInputTestcaseTestNegated() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"modified_negated_input.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==false);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==false);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		
	}
	@Test
	public void simpleChecks() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"simplechecks.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==false);
		Assert.assertTrue(result[2]==true);
		
	}
	@Test
	public void simpleInfinityCheck() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"SimpleInfinityCase.txt");
		Assert.assertTrue(result[0]==false);
		
	}
	@Test
	public void NintendoCheck() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Nintendo.txt");
		Assert.assertTrue(result[0]==true);
		
	}
	@Test
	public void ExamBackwardChaining() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Exam_question_backwardchaining.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==false);
		Assert.assertTrue(result[2]==true);
		
	}
	@Test
	public void ExamBackwardChainingUpdated() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Exam_question_backwardchaining_updated.txt");
		Assert.assertTrue(result[0]==false);
		
	}
	@Test
	public void SampleCase1() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"SampleCase1.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==true);
		Assert.assertTrue(result[5]==true);
		Assert.assertTrue(result[6]==true);
		Assert.assertTrue(result[7]==true);
		Assert.assertTrue(result[8]==true);
		Assert.assertTrue(result[9]==true);
		Assert.assertTrue(result[10]==true);
		
		
	}
	@Test
	public void SampleCase1Modified() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"SampleCase1_modified.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==true);
		Assert.assertTrue(result[5]==true);
		Assert.assertTrue(result[6]==false);
		Assert.assertTrue(result[7]==false);
		Assert.assertTrue(result[8]==false);
		Assert.assertTrue(result[9]==false);
		Assert.assertTrue(result[10]==false);
		
		
	}
	@Test
	public void BuffaloBob() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"BuffaloBob.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==false);
		
		
		
	}
	@Test
	public void constantCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"constantscase.txt");
		Assert.assertTrue(result[0]==true);
		}
	@Test
	public void modifiedconstantCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"modifiedConstantCase.txt");
		Assert.assertTrue(result[0]==false);
		}
	@Test
	public void CatCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"CatCase.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==false);
		}
	@Test
	public void negatedCatCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"NegatedCatCase.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==false);
		}
	
	@Test
	public void visheshCase1() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Vishesh1.txt");
		Assert.assertTrue(result[0]==true);
		
		}
	@Test
	public void visheshCase2() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Vishesh2.txt");
		Assert.assertTrue(result[0]==true);
		
		}
	@Test
    public void MyFavoriteCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"myfavorite.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==false);
		Assert.assertTrue(result[2]==false);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==true);
		Assert.assertTrue(result[5]==false);
		}
	@Test
	public void myfav2() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"inputmyfav.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==false);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==false);
		Assert.assertTrue(result[5]==true);
		
		}
	@Test
	public void myfav2_modified() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"inputmyfav_modified.txt");
		Assert.assertTrue(result[0]==true);
		Assert.assertTrue(result[1]==true);
		Assert.assertTrue(result[2]==true);
		Assert.assertTrue(result[3]==true);
		Assert.assertTrue(result[4]==true);
		Assert.assertTrue(result[5]==true);
		Assert.assertTrue(result[6]==false);
		Assert.assertTrue(result[7]==false);
		
		}
	
	
	@Test
	public void parentCase() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"ParentCase.txt");
		Assert.assertTrue(result[0]==false);
		Assert.assertTrue(result[1]==true);		
		}
	
	@Test
	public void flyCase1() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Fly1.txt");
		Assert.assertTrue(result[0]==false);
		
		}
	@Test
	public void flyCase2() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"Fly2.txt");
		Assert.assertTrue(result[0]==true);
		
		}
	@Test
	public void AnirbanInput() throws FileNotFoundException {
		inference problemObject=new inference();
		boolean[] result=problemObject.backwardChainingAlorithm(path +"AnirbanInput.txt");
		Assert.assertTrue(result[0]==true);
		
		}
	

}
