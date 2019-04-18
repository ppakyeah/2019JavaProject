package minesweeper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Frame extends JFrame {
	JFrame frame = new JFrame();
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();

	final int size = 9;
	final int landmine = 10;
	int Mine_count = landmine;
	int Mine_check = landmine;
	int board[][] = new int[size][size];
	int clickCount[][] = new int[size][size];
	JButton[][] btn = new JButton[size][size];
	int showboard[][] = new int[size][size];
	ImageIcon flagImage = new ImageIcon("../images/key.png");
	ImageIcon questionMark = new ImageIcon("../images/question.png");
	ImageIcon clearImage = new ImageIcon("../images/gold.png");
	ImageIcon treasureBox = new ImageIcon("../images/box.png");
	ImageIcon gameStart = new ImageIcon("../images/gamestart_btn.png");
	ImageIcon gameStart_press = new ImageIcon("../images/gamestart_btn_pressed.png");
	

	JLabel top_hidden_landmine = new JLabel("숨겨진 상자: " + landmine);
	JLabel top_remain_landmine = new JLabel("남은 열쇠: " + Mine_count);
	JButton restart = new JButton("다시 시작하기");
	JButton startButton = new JButton();

	public Frame() {
		setTitle("지뢰찾기");
		frame.setSize(800, 800);
		frame.setResizable(false);
		
		startButton.setIcon(gameStart);
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		startButton.setFocusPainted(false);
		
		panel3.add(startButton);

		// toplabel.setHorizontalAlignment(SwingConstants.CENTER);
		// toplabel.setFont(toplabel.getFont().deriveFont(15.0f));글자크기​
		// Panel에 Layout 적용
		panel1.setLayout(new GridLayout(size, size));
		panel2.setLayout(new FlowLayout());

		// Panel에 그것들을 추가..
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				btn[i][j] = new JButton();
				panel1.add(btn[i][j]);
				btn[i][j].addActionListener(new myActionListener());
				btn[i][j].addMouseListener(new RightMouse());
			}
		}

		panel2.add(top_hidden_landmine);
		panel2.add(restart);
		panel2.add(top_remain_landmine);

		frame.add(panel2, BorderLayout.NORTH);

		frame.add(panel3, BorderLayout.CENTER);
		panel1.setVisible(false);
		panel2.setVisible(false);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				startButton.setIcon(gameStart_press);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				startButton.setIcon(gameStart);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				panel3.setVisible(false);
				panel1.setVisible(true);
				panel2.setVisible(true);
				frame.add(panel1, BorderLayout.CENTER);
				setboard();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			

		});
	}

	public void board_remove(int x, int y) {// 겉 보드 지울 값 넣기
		if (showboard[x][y] == 0) {
			if (board[x][y] == 0) {
				shownum(x, y);
				showboard[x][y] = 1;
				if (x - 1 != -1 && y - 1 != -1)
					board_remove(x - 1, y - 1);
				if (y - 1 != -1)
					board_remove(x, y - 1);
				if (x + 1 != size && y - 1 != -1)
					board_remove(x + 1, y - 1);
				if (x - 1 != -1)
					board_remove(x - 1, y);
				if (x + 1 != size)
					board_remove(x + 1, y);
				if (x - 1 != -1 && y + 1 != size)
					board_remove(x - 1, y + 1);
				if (y + 1 != size)
					board_remove(x, y + 1);
				if (x + 1 != size && y + 1 != size)
					board_remove(x + 1, y + 1);
			} else if (board[x][y] != 8 && board[x][y] > 0) {// 지뢰도 아니고 0도 아닐때
				shownum(x, y);
				showboard[x][y] = 1;
			} else if (board[x][y] == 8) {// 지뢰일때
				gameover();
			}
		}
	}

	public void setboard() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				showboard[i][j] = 0;
				clickCount[i][j] = 0;
			}
		}
		// 지뢰 배정
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = 0;
			}
		}
		int mine_x, mine_y;
		for (int i = 0; i < landmine; i++) {
			mine_x = (int) (Math.random() * size);
			mine_y = (int) (Math.random() * size);
			if (board[mine_x][mine_y] != 8) {
				board[mine_x][mine_y] = 8;
			} else {
				i--;
			}

		}
		// 숫자 배정
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int count = 0;
				if (board[i][j] != 8) {
					if (i - 1 != -1 && j - 1 != -1) {
						if (board[i - 1][j - 1] == 8)
							count++;
					}
					if (j - 1 != -1) {
						if (board[i][j - 1] == 8)
							count++;
					}
					if (i + 1 != size && j - 1 != -1) {
						if (board[i + 1][j - 1] == 8)
							count++;
					}
					if (i - 1 != -1) {
						if (board[i - 1][j] == 8)
							count++;
					}
					if (i + 1 != size) {
						if (board[i + 1][j] == 8)
							count++;
					}
					if (i - 1 != -1 && j + 1 != size) {
						if (board[i - 1][j + 1] == 8)
							count++;
					}
					if (j + 1 != size) {
						if (board[i][j + 1] == 8)
							count++;
					}
					if (i + 1 != size && j + 1 != size) {
						if (board[i + 1][j + 1] == 8)
							count++;
					}
					board[i][j] = count;
				}
			}
		}
	}// setboard

	public void flag(int i, int j) {
		if (showboard[i][j] != 1) {
			btn[i][j].setIcon(flagImage);
			Mine_count--;
			if (board[i][j] == 8) {
				Mine_check--;
			}
			top_remain_landmine.setText("남은 열쇠: " + Mine_count);
		}
		if (Mine_check == 0) {
			gameClear();
		}
	}

	public void questionMark(int i, int j) {
		if (showboard[i][j] != 1) {
			Mine_count++;
			if (board[i][j] == 8) {
				Mine_check++;
			}
		}
		top_remain_landmine.setText("남은 열쇠: " + Mine_count);
		btn[i][j].setIcon(questionMark);
	}

	public void gameClear() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 8) {
					btn[i][j].setIcon(null);
					btn[i][j].setBackground(new Color(255, 128, 0));
					btn[i][j].setIcon(clearImage);
				}
			}
		}
		String gameclear_button[] = { "확인", "메인화면으로" };
		JOptionPane.showOptionDialog(null, "축하합니다! 모든 상자를 열어 보물을 얻었습니다. 다시 시작하시겠습니까?", "GAMECLEAR",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, gameclear_button, "확인");
	}

	public void gameover() {// 게임오버
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (board[i][j] == 8) {
					shownum(i, j);
				}
			}
		}
		String gameover_button[] = { "확인", "메인화면으로" };
		JOptionPane.showOptionDialog(null, "지뢰를 밟았습니다.. 다시 시작하시겠습니까?", "GAMEOVER", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, gameover_button, "확인");
	}

	public void shownum(int x, int y) {// 버튼 비활성화
		btn[x][y].setEnabled(false);
		if (board[x][y] != 0 && board[x][y] != 8)
			btn[x][y].setText(Integer.toString(board[x][y]));
		if (board[x][y] == 8) {
			btn[x][y].setEnabled(true);
			btn[x][y].setIcon(null);

			btn[x][y].setBackground(new Color(255, 128, 0));
			btn[x][y].setIcon(treasureBox);
		}

	}

	public class RightMouse implements MouseListener {// 마우스 우클릭 되었을때
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			if (e.getButton() == MouseEvent.BUTTON3) {

				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						if (btn[i][j] == e.getSource()) {
							clickCount[i][j]++;
							if (clickCount[i][j] == 1)
								flag(i, j);
							else if (clickCount[i][j] == 2)
								questionMark(i, j);
							else {
								btn[i][j].setIcon(null);
								clickCount[i][j] = 0;
							}
						}
					}
				}
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public class myActionListener implements ActionListener {// 버튼 클릭
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (btn[i][j] == e.getSource()) {
						btn[i][j].setIcon(null);
						board_remove(i, j);
					}
				}
			}
		}
	}
}
