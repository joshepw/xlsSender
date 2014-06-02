package resources;


import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.style.INotificationStyle;
import ch.swingfx.twinkle.style.theme.DarkDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import java.util.UUID;
import javax.swing.ImageIcon;

public class Toast {
        private static UUID show;
        private static INotificationStyle style;
	public Toast() {
		// AA the text
		System.setProperty("swing.aatext", "true");

		// First we define the style/theme of the window.
		// Note how we override the default values
		style = new DarkDefaultNotification()
				.withWidth(300) // Optional
				.withAlpha(0.9f) // Optional
		;
	}
        
        public void showItem(String title,String text,String icon){
            show = new NotificationBuilder()
                .withStyle(style) // Required. here we set the previously set style
                .withTitle(title) // Required.
                .withMessage(text) // Optional
                .withIcon(new ImageIcon(Toast.class.getResource( icon +".png"))) // Optional. You could also use a String path
                .withDisplayTime(4000) // Optional
                .withPosition(Positions.SOUTH_EAST) // Optional. Show it at the center of the screen
                .showNotification(); // this returns a UUID that you can use to identify events on the listener
        }

}
