package lab5.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoadingComponent extends JComponent {
    private final int radius;
    private final int width;
    private final int minArcLength;
    private final int maxArcLength;
    private final Color color;

    private double angle = 0;
    private double arcLength = 0;
    private double arcLengthVelocity = 0.5;


    LoadingComponent(int radius, int width, int minArcLength, int maxArcLength, Color color) {
        super();
        this.radius = radius;
        this.width = width;
        this.minArcLength = minArcLength;
        this.maxArcLength = maxArcLength;
        this.color = color;

        new Timer(1000 / 75, actionEvent -> this.repaint()).start();
    }

    LoadingComponent(int radius, int width, Color color) {
        this(radius, width, 45, 300, color);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((this.radius + this.width) * 2, (this.radius + this.width) * 2);
    }

    @Override
    public void paint(Graphics g) {
        // Magic
        // It's based on the spring equation F = -kx
        this.arcLengthVelocity += ((double) (this.minArcLength + this.maxArcLength) / 2 - this.arcLength) / 200;
        this.arcLength += this.arcLengthVelocity > 0 ? this.arcLengthVelocity : this.arcLengthVelocity / 2;

        if (this.arcLength > this.maxArcLength)
            this.arcLength = this.maxArcLength;
        else if (this.arcLength < this.minArcLength)
            this.arcLength = this.minArcLength;

        this.angle += 4;
        if (this.arcLengthVelocity < 0)
            this.angle -= this.arcLengthVelocity / 2;


        if (this.angle > 360)
            this.angle -= 360;

        var bufferImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var g2 = (Graphics2D) bufferImage.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 0, 0, 0));
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.setColor(this.color);
        g2.setStroke(new BasicStroke(this.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        g2.drawArc(this.getWidth()/2 - this.radius, this.getHeight()/2 - this.radius, this.radius * 2, this.radius * 2, (int)this.angle, (int)this.arcLength);

        g.drawImage(bufferImage, 0, 0, null);
    }

}
