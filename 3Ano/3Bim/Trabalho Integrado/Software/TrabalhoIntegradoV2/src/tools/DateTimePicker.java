package tools;

import java.awt.Color;
import java.awt.FlowLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.DefaultsList;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.plaf.basic.BasicDatePickerUI;

@SuppressWarnings("serial")
public class DateTimePicker extends JXDatePicker {
	JSpinner secSpinner;
	JSpinner minSpinner;
	JSpinner hourSpinner;
	
	private static final String secs[] = createTimeString(60);
	private static final String mins[] = createTimeString(60);
	private static final String hours[] = createTimeString(24);

	/**
	 * UI Class ID
	 */
	public static final String uiClassID = "DateTimePickerUI";

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public DateTimePicker() {
		super();

		// set the SingleDaySelectionModel to enable the time component
		getMonthView().setSelectionModel(new SingleDaySelectionModel());
		prepareLinkPanel();
	}

	public JPanel prepareLinkPanel() {

		JPanel newPanel = new JPanel();
		newPanel.setLayout(new FlowLayout());

		SpinnerListModel hourModel = new SpinnerListModel(createTimeString(24));
		hourSpinner = new JSpinner(hourModel);
		hourSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeDate();
			}
		});

		SpinnerListModel minModel = new SpinnerListModel(createTimeString(60));
		minSpinner = new JSpinner(minModel);
		minSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeDate();
			}
		});
		
		SpinnerListModel secModel = new SpinnerListModel(createTimeString(60));
		secSpinner = new JSpinner(secModel);
		secSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changeDate();
			}
		});

		newPanel.add(new JLabel("Horario:"));
		newPanel.add(hourSpinner);
		newPanel.add(new JLabel(":"));
		newPanel.add(minSpinner);
		newPanel.add(new JLabel(":"));
		newPanel.add(secSpinner);
		newPanel.setBackground(Color.WHITE);

		setLinkPanel(newPanel);

		return newPanel;
	}

	private void changeDate() {
		// do not change the date with every spinner because this fires many
		// events
		// better use an apply or ok button

		Date date = getDate();
		if (date != null) {
			int hours = Integer.parseInt((String) hourSpinner.getValue());
			int min = Integer.parseInt((String) minSpinner.getValue());
			int sec = Integer.parseInt((String) secSpinner.getValue());


			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			// use set instead of add to set the time values
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.SECOND, sec);
			calendar.set(Calendar.MILLISECOND, 0);

			Date newDate = calendar.getTime();
			setDate(newDate);
		}
	}

	public Date getDate() {
		Date date = super.getDate();
		if (date != null) {
			int hours = Integer.parseInt((String) hourSpinner.getValue());
			int min = Integer.parseInt((String) minSpinner.getValue());
			int sec = Integer.parseInt((String) secSpinner.getValue());

			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			// use set instead of add to set the time values
			calendar.set(Calendar.HOUR_OF_DAY, hours);
			calendar.set(Calendar.MINUTE, min);
			calendar.set(Calendar.SECOND, sec);
			calendar.set(Calendar.MILLISECOND, 0);

			Date newDate = calendar.getTime();

			date = newDate;
		}
		return date;
	}

	public void setDate(Date date) {
		if (date != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);
			int sec = calendar.get(Calendar.SECOND);

			try {
				hourSpinner.setValue(hours[hour]);
				minSpinner.setValue(mins[min]);
				secSpinner.setValue(secs[sec]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.setDate(date);
	}

	private static String[] createTimeString(int time) {
		String[] retString = new String[time];
		for (int i = 0; i < retString.length; i++) {
			String timeS = "";

			if (i < 10)
				timeS = "0";
			timeS += Integer.toString(i);
			retString[i] = timeS;
		}
		return retString;
	}

	/**
	 * where should I call this method???? --> from BasicDateTimePickerUI
	 */
	public void setTimeSpinners(Date date) {
		if (date != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int min = calendar.get(Calendar.MINUTE);
			int sec = calendar.get(Calendar.SECOND);

			
			try {
				hourSpinner.setValue(hours[hour]);
				minSpinner.setValue(mins[min]);
				secSpinner.setValue(secs[sec]);
			} catch (Exception ex) {
				System.out.println("failed to set the spinners.");
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	public static class BasicDateTimePickerUI extends BasicDatePickerUI {

		/**
		 * Updates internals after picker's date property changed.
		 */
		protected void updateFromDateChanged() {
			Date visibleHook = datePicker.getDate() != null ? datePicker
					.getDate() : datePicker.getLinkDay();
			datePicker.getMonthView().ensureDateVisible(visibleHook);
			datePicker.getEditor().setValue(datePicker.getDate());
			// this will update the spinners
			((DateTimePicker) datePicker).setTimeSpinners(datePicker.getDate());
		}

		public static ComponentUI createUI(JComponent c) {
			return new BasicDateTimePickerUI();
		}
	}

	private static class MyDatePickerAddon extends AbstractComponentAddon {
		public MyDatePickerAddon() {
			super("DateTimePicker");
		}

		@Override
		protected void addBasicDefaults(LookAndFeelAddons addon,
				DefaultsList defaults) {
			super.addBasicDefaults(addon, defaults);

			defaults.add(DateTimePicker.uiClassID,
					BasicDateTimePickerUI.class.getName());
			defaults.add(
					"DateTimePicker.border",
					new BorderUIResource(BorderFactory.createCompoundBorder(
							LineBorder.createGrayLineBorder(),
							BorderFactory.createEmptyBorder(3, 3, 3, 3))));

			UIManagerExt
					.addResourceBundle("org.jdesktop.swingx.plaf.basic.resources.DatePicker");
		}
	}

	static {
		LookAndFeelAddons.contribute(new MyDatePickerAddon());
	}

}