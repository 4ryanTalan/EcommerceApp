import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;


// FRONTEND / BACKEND ENGINE

public class EcommerceApp extends JFrame {

    // Backend State
    private final List<Product> storeCatalog = new ArrayList<>();
    private final Map<String, CartItem> cart = new HashMap<>();
    private String activeCategoryFilter = "All Categories";

    // GUI UI Components
    private JTable productTable;
    private DefaultTableModel productTableModel;
    private JTable cartTable;
    private DefaultTableModel cartTableModel;

    private JLabel totalLabel;
    private JTextField searchField;
    private JComboBox<String> sortDropdown;
    private JPanel categorySidebar;

    // Theme Custom Color Palette
    private final Color ACCENT_COLOR = new Color(95, 199, 211);  // Forest Green
    private final Color BG_DARK = new Color(40, 44, 52);        // Dark Charcoal
    private final Color TEXT_LIGHT = Color.WHITE;

    public EcommerceApp() {
        initCatalog();

        // Core App Windows Setup
        setTitle("HeisenBerg Premium Shopping Suite");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // --- MODERN FLAT HEADER ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_DARK);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("HEISENBERG DIGITAL STOREFRONT", JLabel.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(TEXT_LIGHT);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // --- MASTER CONTAINER ---
        JPanel mainContentWorkspace = new JPanel(new BorderLayout(10, 10));
        mainContentWorkspace.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainContentWorkspace.add(createCategorySidebar(), BorderLayout.WEST);

        JSplitPane horizontalSplitArea = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createCatalogPanel(), createCartPanel());
        horizontalSplitArea.setDividerLocation(480);
        horizontalSplitArea.setBorder(null);
        mainContentWorkspace.add(horizontalSplitArea, BorderLayout.CENTER);

        add(mainContentWorkspace, BorderLayout.CENTER);

        applyFiltersAndSorting();
    }

    private void initCatalog() {
        storeCatalog.add(new Product("G01", "PlayStation 5 Console (Slim)", "Consoles", 499.99, 12));
        storeCatalog.add(new Product("G02", "Xbox Series X 1TB", "Consoles", 499.99, 8));
        storeCatalog.add(new Product("G03", "Nintendo Switch OLED Model", "Consoles", 349.99, 20));
        storeCatalog.add(new Product("G04", "PlayStation VR2 Headset", "Consoles", 549.99, 4));

        storeCatalog.add(new Product("P01", "Gaming Laptop RTX 4070", "Hardware", 1399.99, 5));
        storeCatalog.add(new Product("P02", "UltraWide Gaming Monitor 34\"", "Hardware", 349.00, 7));
        storeCatalog.add(new Product("P03", "Mechanical Keyboard (RGB)", "Hardware", 89.95, 25));
        storeCatalog.add(new Product("P04", "Wireless Gaming Mouse", "Hardware", 45.50, 40));
        storeCatalog.add(new Product("P05", "External SSD 2TB", "Hardware", 129.99, 15));

        storeCatalog.add(new Product("A01", "Noise Cancelling Headphones", "Audio", 149.99, 18));
        storeCatalog.add(new Product("A02", "PS5 DualSense Controller", "Audio", 69.99, 30));
        storeCatalog.add(new Product("A03", "Xbox Wireless Controller", "Audio", 59.99, 22));
        storeCatalog.add(new Product("A04", "Streaming Microphone XLR", "Audio", 119.50, 10));

        storeCatalog.add(new Product("S01", "4K Smart OLED TV 55\"", "Smart Tech", 899.99, 6));
        storeCatalog.add(new Product("S02", "Smart Watch Series 9", "Smart Tech", 399.00, 14));
        storeCatalog.add(new Product("S03", "Wireless Charging Pad", "Smart Tech", 19.99, 50));
    }

    private JPanel createCategorySidebar() {
        categorySidebar = new JPanel();
        categorySidebar.setLayout(new BoxLayout(categorySidebar, BoxLayout.Y_AXIS));
        categorySidebar.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Categories",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)));
        categorySidebar.setPreferredSize(new Dimension(160, 0));

        String[] menuCategories = {"All Categories", "Consoles", "Hardware", "Audio", "Smart Tech"};

        for (String categoryName : menuCategories) {
            JButton catButton = new JButton(categoryName);
            catButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            catButton.setMaximumSize(new Dimension(145, 35));
            catButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            catButton.setFocusPainted(false);

            // RENDERING FIX: Standardize properties to force drawing control
            catButton.setOpaque(true);

            if(categoryName.equals(activeCategoryFilter)) {
                catButton.setContentAreaFilled(true);
                catButton.setBackground(ACCENT_COLOR);
                catButton.setForeground(Color.WHITE);
                catButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker()));
            } else {
                catButton.setContentAreaFilled(false);
                catButton.setBackground(Color.WHITE);
                catButton.setForeground(Color.BLACK);
                catButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            }

            catButton.addActionListener(e -> {
                activeCategoryFilter = categoryName;
                for (Component comp : categorySidebar.getComponents()) {
                    if (comp instanceof JButton) {
                        JButton btn = (JButton) comp;
                        btn.setContentAreaFilled(false);
                        btn.setBackground(Color.WHITE);
                        btn.setForeground(Color.BLACK);
                        btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                    }
                }
                catButton.setContentAreaFilled(true);
                catButton.setBackground(ACCENT_COLOR);
                catButton.setForeground(Color.WHITE);
                catButton.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker()));
                applyFiltersAndSorting();
            });

            categorySidebar.add(catButton);
            categorySidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        return categorySidebar;
    }

    private void applyFiltersAndSorting() {
        productTableModel.setRowCount(0);
        List<Product> outputDisplayList = new ArrayList<>();
        String searchText = searchField.getText().toLowerCase().trim();

        for (Product item : storeCatalog) {
            boolean matchesCategory = activeCategoryFilter.equals("All Categories") || item.getCategory().equals(activeCategoryFilter);
            boolean matchesSearch = item.getName().toLowerCase().contains(searchText) || item.getId().toLowerCase().contains(searchText);

            if (matchesCategory && matchesSearch) {
                outputDisplayList.add(item);
            }
        }

        int selectedSortIndex = sortDropdown.getSelectedIndex();
        if (selectedSortIndex == 0) {
            outputDisplayList.sort(Comparator.comparing(Product::getName));
        } else if (selectedSortIndex == 1) {
            outputDisplayList.sort(Comparator.comparingDouble(Product::getPrice));
        } else if (selectedSortIndex == 2) {
            outputDisplayList.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        }

        for (Product p : outputDisplayList) {
            productTableModel.addRow(new Object[]{
                    p.getId(), p.getName(), String.format("$%.2f", p.getPrice()), p.getInventoryStock() + " units"
            });
        }
    }

    private JPanel createCatalogPanel() {
        JPanel containerPanel = new JPanel(new BorderLayout(10, 10));
        containerPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Product Selection",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)));

        JPanel utilityControlStrip = new JPanel(new GridBagLayout());
        utilityControlStrip.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 5);

        searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(140, 28));
        searchField.addCaretListener(e -> applyFiltersAndSorting());

        String[] sortOptions = {"Sort: A-Z", "Price: Low to High", "Price: High to Low"};
        sortDropdown = new JComboBox<>(sortOptions);
        sortDropdown.addActionListener(e -> applyFiltersAndSorting());

        gbc.weightx = 0.6; gbc.gridx = 0;
        utilityControlStrip.add(new JLabel("Search: "), gbc);
        gbc.gridx = 1; utilityControlStrip.add(searchField, gbc);

        gbc.weightx = 0.4; gbc.gridx = 2;
        utilityControlStrip.add(sortDropdown, gbc);

        containerPanel.add(utilityControlStrip, BorderLayout.NORTH);

        String[] columns = {"ID", "Product Name", "Unit Price", "Stock Availability"};
        productTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        productTable = new JTable(productTableModel);
        productTable.setRowHeight(24);
        productTable.getTableHeader().setReorderingAllowed(false);
        containerPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);

        JButton addToCartBtn = new JButton("Add Selected to Cart") {
            @Override
            protected void paintComponent(Graphics g) {
                // Manually draw high-contrast background to bypass OS theme glitch
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        addToCartBtn.setOpaque(false);
        addToCartBtn.setContentAreaFilled(false);
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker()));
        addToCartBtn.setPreferredSize(new Dimension(0, 38));
        addToCartBtn.addActionListener(e -> handleAddToCart());
        containerPanel.add(addToCartBtn, BorderLayout.SOUTH);

        return containerPanel;
    }

    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Your Shopping Cart",
                TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 13)));

        String[] columns = {"Item", "Base Price", "Qty", "Subtotal"};
        cartTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(24);
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 8, 8));
        JButton updateBtn = new JButton("Modify Quantity");
        JButton removeBtn = new JButton("Remove Item");

        updateBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        removeBtn.addActionListener(e -> handleRemoveItem());
        updateBtn.addActionListener(e -> handleUpdateQuantity());
        controlPanel.add(updateBtn);
        controlPanel.add(removeBtn);

        JPanel bottomActionContainer = new JPanel(new BorderLayout(5, 5));
        bottomActionContainer.add(controlPanel, BorderLayout.NORTH);

        totalLabel = new JLabel("Total: $0.00", JLabel.RIGHT);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setBorder(BorderFactory.createEmptyBorder(12, 5, 12, 5));
        bottomActionContainer.add(totalLabel, BorderLayout.CENTER);

        // RENDERING FIX: Checkout Custom Look Configuration
        JButton checkoutBtn = new JButton("Proceed to Order Checkout") {
            @Override
            protected void paintComponent(Graphics g) {
                // Manually draw high-contrast background to bypass OS theme glitch
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT_COLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        checkoutBtn.setOpaque(false);
        checkoutBtn.setContentAreaFilled(false);
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR.darker()));
        checkoutBtn.setPreferredSize(new Dimension(0, 42));
        checkoutBtn.addActionListener(e -> handleCheckout());
        bottomActionContainer.add(checkoutBtn, BorderLayout.SOUTH);

        panel.add(bottomActionContainer, BorderLayout.SOUTH);
        return panel;
    }

    private Product getProductFromCatalogById(String id) {
        for(Product p : storeCatalog) {
            if(p.getId().equals(id)) return p;
        }
        return null;
    }

    private void handleAddToCart() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the catalog table first.", "Selection Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String prodId = (String) productTable.getValueAt(selectedRow, 0);
        Product selectedProduct = getProductFromCatalogById(prodId);

        if (selectedProduct == null) return;

        if (selectedProduct.getInventoryStock() <= 0) {
            JOptionPane.showMessageDialog(this, "Sorry, " + selectedProduct.getName() + " is currently completely out of stock!", "Stock Shortage", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to buy (" + selectedProduct.getInventoryStock() + " available):", "1");
        if (qtyStr == null) return;

        try {
            int requestedQty = Integer.parseInt(qtyStr.trim());
            if (requestedQty <= 0) throw new NumberFormatException();

            int currentCartQty = cart.containsKey(prodId) ? cart.get(prodId).getQuantity() : 0;
            if (currentCartQty + requestedQty > selectedProduct.getInventoryStock()) {
                JOptionPane.showMessageDialog(this, "Cannot add requested volume. Exceeds current available warehouse storage inventory units.", "Stock Overflow", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cart.containsKey(prodId)) {
                CartItem item = cart.get(prodId);
                item.setQuantity(item.getQuantity() + requestedQty);
            } else {
                cart.put(prodId, new CartItem(selectedProduct, requestedQty));
            }

            refreshCartUI();
            applyFiltersAndSorting();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive non-zero whole integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRemoveItem() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please choose an active purchase row inside your cart to delete.", "Selection Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] productIdsInCart = cart.keySet().toArray(new String[0]);
        String targetId = productIdsInCart[selectedRow];

        cart.remove(targetId);
        refreshCartUI();
        applyFiltersAndSorting();
    }

    private void handleUpdateQuantity() {
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an item in your cart to change its quantity.", "Selection Needed", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] productIdsInCart = cart.keySet().toArray(new String[0]);
        String targetId = productIdsInCart[selectedRow];
        CartItem item = cart.get(targetId);
        Product baseProduct = item.getProduct();

        String qtyStr = JOptionPane.showInputDialog(this, "Update quantity for " + baseProduct.getName() + " (Max Available: " + baseProduct.getInventoryStock() + "):", item.getQuantity());
        if (qtyStr == null) return;

        try {
            int newQty = Integer.parseInt(qtyStr.trim());
            if (newQty <= 0) {
                cart.remove(targetId);
            } else {
                if(newQty > baseProduct.getInventoryStock()) {
                    JOptionPane.showMessageDialog(this, "Requested quantity exceeds available stock levels (" + baseProduct.getInventoryStock() + ").", "Over-allocation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                item.setQuantity(newQty);
            }
            refreshCartUI();
            applyFiltersAndSorting();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please write down a valid clean whole integer number entry.", "Invalid Syntax", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Add items before checking out!", "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double finalTotal = calculateGrandTotal();

        StringBuilder receiptBuilder = new StringBuilder();
        receiptBuilder.append("==== HEISENBERG TRANSACTION INVOICE ====\n\n");
        for (CartItem item : cart.values()) {
            receiptBuilder.append(String.format("%s (x%d) - Subtotal: $%.2f%n",
                    item.getProduct().getName(), item.getQuantity(), item.getTotalPrice()));
        }
        receiptBuilder.append(String.format("\nGRAND TOTAL DUE: $%.2f%n", finalTotal));
        receiptBuilder.append("\nConfirm allocation and authorize secure checkout payment routing?");

        int choice = JOptionPane.showConfirmDialog(this, receiptBuilder.toString(), "Secure Transaction Terminal", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {

            for (CartItem item : cart.values()) {
                Product p = item.getProduct();
                p.decreaseStock(item.getQuantity());
            }

            JOptionPane.showMessageDialog(this, "Payment Authorized via Secure Merchant API Gateway!\nYour item dispatch tracking schedule is compiling.", "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            cart.clear();
            refreshCartUI();
            applyFiltersAndSorting();
        }
    }

    private double calculateGrandTotal() {
        double total = 0.0;
        for (CartItem item : cart.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    private void refreshCartUI() {
        cartTableModel.setRowCount(0);
        for (CartItem item : cart.values()) {
            cartTableModel.addRow(new Object[]{
                    item.getProduct().getName(),
                    String.format("$%.2f", item.getProduct().getPrice()),
                    item.getQuantity(),
                    String.format("$%.2f", item.getTotalPrice())
            });
        }
        totalLabel.setText(String.format("Total: $%.2f", calculateGrandTotal()));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new EcommerceApp().setVisible(true));
    }
}