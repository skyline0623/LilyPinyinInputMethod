package Running;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;  
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;


public class CInputMehod extends JPanel implements KeyListener, ListSelectionListener{  
  //
      
    private static final long serialVersionUID = 1L;  
  
    public static void main(String args[]) throws Exception { 
    	CInputMehod plist = new CInputMehod(5);  
        
       
        JFrame f = new JFrame("拼音输入法 v1.0");  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(plist);          
        f.setMinimumSize(new Dimension(380, 80));        

        f.pack();  
        f.setVisible(true);  
    }  
	
    private int pageSize;  
    private  JList list;  
    public JList getList() {
		return list;
	}

	private ListModel model;  
	
    private int lastPageNum;  
    private int currPageNum;  

    private JButton prev, next;  
    
    
    private List<String> listContainer;
    protected JTextField textField;
    
    public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}
    protected  JTextArea textArea;
    public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	protected JScrollPane scrollPane;
	
    public CInputMehod(int pageSize) {  
    	super();     	
    	listContainer = new ArrayList<String>();   
    	this.list = new JList();
    	this.pageSize = pageSize;    
        setLayout(new BorderLayout());  
        textField = new JTextField(20);
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);        
        scrollPane.setAutoscrolls(true);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(200, 100));      
        add(textField, BorderLayout.NORTH);      
        add(this.list, BorderLayout.WEST);  
        add(createControls(), BorderLayout.EAST);  
        add(scrollPane, BorderLayout.SOUTH);
        textField.addKeyListener(this);
        //list.addListSelectionListener(this);
        this.list.setLayoutOrientation(JList.HORIZONTAL_WRAP);//横向显示
        this.list.setVisibleRowCount(1);    
        this.list.setMinimumSize(new Dimension(next.getSize()));
        prev.setEnabled(false);  
        next.setEnabled(false);  
    }  
  
    private JPanel createControls() {     
        prev = new JButton(new AbstractAction("前一页") {  
            private static final long serialVersionUID = 1L;  
  
            public void actionPerformed(ActionEvent e) {  
                if (--currPageNum <= 0)  
                    currPageNum = 1;  
                updatePage();  
            }  
        });  
  
        next = new JButton(new AbstractAction("后一页") {  
            private static final long serialVersionUID = 1L;  
  
            public void actionPerformed(ActionEvent e) {  
                if (++currPageNum > lastPageNum)  
                    currPageNum = lastPageNum;  
                updatePage();  
  
            }  
        });  
    
        JPanel bar = new JPanel(new GridLayout(1, 2));  
        bar.add(prev);  
        bar.add(next);  
        return bar;  
    }  
  
    private void updatePage() {   
        final DefaultListModel page = new DefaultListModel();  
        final int start = (currPageNum - 1) * pageSize;  
        int end = start + pageSize;  
        if (end >= model.getSize()) {  
            end = model.getSize();  
        }  
        for (int i = start; i < end; i++) {  
            page.addElement(model.getElementAt(i));  
        }  
        list.setModel(page);          

        final boolean canGoBack = currPageNum != 1;  
        final boolean canGoFwd = ((currPageNum != lastPageNum) && (lastPageNum > 0));  
        prev.setEnabled(canGoBack);  
        next.setEnabled(canGoFwd);  
    }


	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		list.setSelectedIndex(e.getFirstIndex());
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		   if (e.getValueIsAdjusting() == false) {		        
		        	JList list = (JList)e.getSource();
					int selections[] = list.getSelectedIndices();
					Object selectionValues[] = list.getSelectedValues();
					for(int i=0, n=selections.length; i<n; i++) {						
						pw.print(((String)selectionValues[i]).charAt(3));
					}
					getTextArea().append(sw.toString());        		        
		    }
		   textField.setText("");
//		   textArea.setCaretPosition(textArea.getDocument().getLength());
//		   textField.selectAll();
	}
	
	private void addAllElements(Iterator<Word> iter){
		int i = 0;
		while(iter.hasNext()){
			listContainer.add(((i++)%pageSize)+1 +  ". " + iter.next().getWord() + "     ");
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		 listContainer.clear();
		 String inputStr = textField.getText(); 
		 if(!textField.getText().equals(e.getKeyChar()+"") && e.getKeyChar() >= '1' && e.getKeyChar() < '6'){	        	   
//      	   System.out.println(e.getKeyChar());
      	   int index = list.getNextMatch(e.getKeyChar()+"", 0, Position.Bias.Backward);
//      	   System.out.println(index);
      	   valueChanged(new ListSelectionEvent(list, index, index, false));	
      	   inputStr = "";
		 }
		 else if(!textField.getText().equals(e.getKeyChar()+"") && e.getKeyChar() == ' '){	        	   
//      	   System.out.println(e.getKeyChar());
//      	   System.out.println(index);
      	   valueChanged(new ListSelectionEvent(list, 0, 0, false));	
      	   inputStr = "";
		 }
		 if(!textField.getText().equals(e.getKeyChar()+"") && e.getKeyChar() == '6'){
			 if (++currPageNum > lastPageNum)  
                 currPageNum = lastPageNum;  
             updatePage();
             textField.setText(inputStr.substring(0, inputStr.length() - 1));
             return;
		 }
		 if(!textField.getText().equals(e.getKeyChar()+"") && e.getKeyChar() == '`'){
			 if (--currPageNum <= 0)  
                 currPageNum = 1;  
             updatePage();
             textField.setText(inputStr.substring(0, inputStr.length() - 1));
             return;
		 }
		 //查询得到拼音对应的汉字
		 if(inputStr.length() > 0)
			 this.addAllElements(Process.getCandidates(inputStr));
		//然后调用addAllElements
		
		 
		 String[] str = new String[listContainer.size()];
		 listContainer.toArray(str);
		 list.setListData(str);		 
	     list.setLayoutOrientation(JList.HORIZONTAL_WRAP);//横向显示
	     list.setVisibleRowCount(1);  
	     this.model = list.getModel(); 	      
	     this.lastPageNum = model.getSize() / pageSize  
	                + (model.getSize() % pageSize != 0 ? 1 : 0);  
	     this.currPageNum = 1;	     
	     updatePage();  
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}  