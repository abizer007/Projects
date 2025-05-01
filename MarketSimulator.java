
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

class Stock {

    private String name;
    private String symbol;
    private double price;
    private double lastChange = 0.0;

    public Stock(String name, String symbol, double price) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getLastChange() {
        return lastChange;
    }

    public void updatePrice() {
        Random random = new Random();
        double oldPrice = price;
        price += (random.nextDouble() - 0.5) * 100;
        if (price < 1.0) {
            price = 1.0;
        }
        lastChange = price - oldPrice;
    }

    @Override
    public String toString() {
        return String.format("%s (%s): ₹%.2f (%+.2f)", name, symbol, price, lastChange);
    }
}

class Trader {

    private String name;
    private double balance;
    private HashMap<String, Integer> portfolio = new HashMap<>();
    private ArrayList<String> transactionHistory = new ArrayList<>();

    public Trader(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void buyStock(Stock stock, int amount) throws Exception {
        double cost = stock.getPrice() * amount;
        if (balance < cost) {
            throw new Exception("Insufficient balance to buy " + stock.getName());
        }
        balance -= cost;
        portfolio.put(stock.getSymbol(), portfolio.getOrDefault(stock.getSymbol(), 0) + amount);
        transactionHistory.add("Bought " + amount + " x " + stock.getSymbol() + " @ ₹" + String.format("%.2f", stock.getPrice()));
    }

    public void sellStock(Stock stock, int amount) throws Exception {
        int ownedAmount = portfolio.getOrDefault(stock.getSymbol(), 0);
        if (ownedAmount < amount) {
            throw new Exception("Not enough shares to sell " + stock.getName());
        }
        balance += stock.getPrice() * amount;
        portfolio.put(stock.getSymbol(), ownedAmount - amount);
        transactionHistory.add("Sold " + amount + " x " + stock.getSymbol() + " @ ₹" + String.format("%.2f", stock.getPrice()));
    }

    public int getOwnedShares(Stock stock) {
        return portfolio.getOrDefault(stock.getSymbol(), 0);
    }

    public double getPortfolioValue(ArrayList<Stock> stocks) {
        double value = 0.0;
        for (Stock stock : stocks) {
            value += stock.getPrice() * getOwnedShares(stock);
        }
        return value;
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    @Override
    public String toString() {
        return name + " - Balance: ₹" + String.format("%.2f", balance);
    }
}

class StockMarket {

    private ArrayList<Stock> stocks = new ArrayList<>();

    public void addStock(Stock stock) {
        stocks.add(stock);
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public void simulateDay() {
        for (Stock stock : stocks) {
            stock.updatePrice();
        }
    }
}

public class MarketSimulator extends JFrame {

    private StockMarket stockMarket;
    private Trader trader;
    private JTextArea marketArea, traderArea, historyArea;
    private JPanel stockPanel;

    public MarketSimulator(StockMarket stockMarket, Trader trader) {
        this.stockMarket = stockMarket;
        this.trader = trader;

        setTitle("Stock Market Simulator");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        marketArea = new JTextArea();
        traderArea = new JTextArea();
        historyArea = new JTextArea();
        marketArea.setEditable(false);
        traderArea.setEditable(false);
        historyArea.setEditable(false);

        JPanel infoPanel = new JPanel(new GridLayout(1, 3));
        infoPanel.add(wrapInScrollPane(marketArea, "Market Info"));
        infoPanel.add(wrapInScrollPane(traderArea, "Trader Info"));
        infoPanel.add(wrapInScrollPane(historyArea, "Transaction History"));

        stockPanel = new JPanel(new GridLayout(0, 1));
        populateStockPanel();

        JButton simulateButton = new JButton("Simulate Day");
        simulateButton.addActionListener(e -> {
            stockMarket.simulateDay();
            updateAllAreas();
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(simulateButton, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(stockPanel), BorderLayout.CENTER);

        add(infoPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        updateAllAreas();
    }

    private JScrollPane wrapInScrollPane(JTextArea area, String title) {
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), title));
        return scrollPane;
    }

    private void populateStockPanel() {
        stockPanel.removeAll();
        for (Stock stock : stockMarket.getStocks()) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(stock.toString());
            JButton buy = new JButton("Buy");
            JButton sell = new JButton("Sell");

            buy.addActionListener(e -> {
                try {
                    trader.buyStock(stock, 1);
                    updateAllAreas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Buy Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            sell.addActionListener(e -> {
                try {
                    trader.sellStock(stock, 1);
                    updateAllAreas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Sell Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(label);
            panel.add(buy);
            panel.add(sell);
            stockPanel.add(panel);
        }
    }

    private void updateAllAreas() {
        updateMarketArea();
        updateTraderArea();
        updateHistoryArea();
        populateStockPanel();
        revalidate();
        repaint();
    }

    private void updateMarketArea() {
        marketArea.setText("Stock Market:\n");
        for (Stock stock : stockMarket.getStocks()) {
            marketArea.append(stock.toString() + "\n");
        }
    }

    private void updateTraderArea() {
        traderArea.setText(trader.toString() + "\nPortfolio Value: ₹"
                + String.format("%.2f", trader.getPortfolioValue(stockMarket.getStocks())) + "\n\n");
        for (Stock stock : stockMarket.getStocks()) {
            int shares = trader.getOwnedShares(stock);
            if (shares > 0) {
                traderArea.append(stock.getSymbol() + ": " + shares + " shares\n");
            }
        }
    }

    private void updateHistoryArea() {
        historyArea.setText("Recent Transactions:\n");
        for (String entry : trader.getTransactionHistory()) {
            historyArea.append(entry + "\n");
        }
    }

    public static void main(String[] args) {
        StockMarket stockMarket = new StockMarket();
        stockMarket.addStock(new Stock("Reliance Industries", "RELIANCE", 2300.0));
        stockMarket.addStock(new Stock("Tata Consultancy", "TCS", 3400.0));
        stockMarket.addStock(new Stock("HDFC Bank", "HDFC", 1500.0));
        stockMarket.addStock(new Stock("Infosys", "INFY", 1300.0));
        stockMarket.addStock(new Stock("ICICI Bank", "ICICI", 800.0));
        stockMarket.addStock(new Stock("Bharti Airtel", "AIRTEL", 700.0));
        stockMarket.addStock(new Stock("State Bank of India", "SBI", 450.0));
        stockMarket.addStock(new Stock("Kotak Mahindra", "KOTAK", 1750.0));
        stockMarket.addStock(new Stock("Larsen & Toubro", "LT", 1600.0));
        stockMarket.addStock(new Stock("Axis Bank", "AXIS", 820.0));

        Trader trader = new Trader("Trader1", 100000.0);

        SwingUtilities.invokeLater(() -> {
            new MarketSimulator(stockMarket, trader).setVisible(true);
        });
    }
}
