// Code d'interface graphique Swing pour l'agence (Partie D - Bonus)
// Inspiré directement de l'architecture et de la structure de SwingUI du TP1 (HAI704I-TP1/client/src/cabinet/SwingUI.java)

package hai704i.agence.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import hai704i.agence.application.LogiqueAgence;
import hai704i.agence.application.OffreVue;
import hai704i.agence.application.ResultatReservation;

public class SwingUI extends JFrame 
{
    private final LogiqueAgence service;

    // Champs de recherche avec valeurs par défaut pratiques
    private final JTextField champVille = new JTextField("Montpellier", 10);
    private final JTextField champArrivee = new JTextField("01-12-2026", 8);
    private final JTextField champDepart = new JTextField("06-12-2026", 8);
    private final JTextField champPersonnes = new JTextField("2", 3);
    private final JTextField champEtoiles = new JTextField("3", 2);
    private final JTextField champBudget = new JTextField("1000", 5);

    // Tableau des offres
    private final DefaultTableModel modeleTable;
    private final JTable tableOffres;
    private final List<OffreVue> offresCourantes = new ArrayList<>();

    // Barre d'état
    private final JLabel labelStatut = new JLabel("Prêt.");

    public SwingUI(LogiqueAgence service) 
    {
        super("Agence de Voyages - Interface Graphique (Partie D)");
        this.service = service;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // Panneau supérieur : Filtres de recherche
        JPanel panneauFiltres = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panneauFiltres.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));

        panneauFiltres.add(new JLabel("Ville :"));
        panneauFiltres.add(champVille);
        panneauFiltres.add(new JLabel("Arrivée :"));
        panneauFiltres.add(champArrivee);
        panneauFiltres.add(new JLabel("Départ :"));
        panneauFiltres.add(champDepart);
        panneauFiltres.add(new JLabel("Pers. :"));
        panneauFiltres.add(champPersonnes);
        panneauFiltres.add(new JLabel("Étoiles :"));
        panneauFiltres.add(champEtoiles);
        panneauFiltres.add(new JLabel("Budget max :"));
        panneauFiltres.add(champBudget);

        add(panneauFiltres, BorderLayout.NORTH);

        // Zone centrale : Tableau des offres
        String[] colonnes = { "#", "Hôtel", "Étoiles", "Description Chambre", "Arrivée", "Départ", "Prix Total (€)" };
        modeleTable = new DefaultTableModel(colonnes, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        tableOffres = new JTable(modeleTable);
        tableOffres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableOffres.setRowHeight(24);
        tableOffres.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(new JScrollPane(tableOffres), BorderLayout.CENTER);

        // Panneau inférieur : Boutons d'action et Statut
        JPanel panneauBas = new JPanel(new BorderLayout(5, 5));

        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnRechercher = new JButton("1. Rechercher des offres");
        JButton btnReserver = new JButton("2. Réserver l'offre sélectionnée");
        JButton btnEffacer = new JButton("3. Effacer");

        panneauBoutons.add(btnRechercher);
        panneauBoutons.add(btnReserver);
        panneauBoutons.add(btnEffacer);

        panneauBas.add(panneauBoutons, BorderLayout.NORTH);
        labelStatut.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        panneauBas.add(labelStatut, BorderLayout.SOUTH);

        add(panneauBas, BorderLayout.SOUTH);

        // --- Actions des boutons ---

        // Rechercher
        btnRechercher.addActionListener(e -> lancerRecherche());

        // Réserver
        btnReserver.addActionListener(e -> lancerReservation());

        // Effacer
        btnEffacer.addActionListener(e -> {
            modeleTable.setRowCount(0);
            offresCourantes.clear();
            labelStatut.setText("Tableau réinitialisé.");
        });
    }

    private void lancerRecherche() 
    {
        String ville = champVille.getText().trim();
        String dateArr = champArrivee.getText().trim();
        String dateDep = champDepart.getText().trim();
        int pers;
        int etoiles;
        double budget;

        try 
        {
            pers = Integer.parseInt(champPersonnes.getText().trim());
            etoiles = Integer.parseInt(champEtoiles.getText().trim());
            budget = Double.parseDouble(champBudget.getText().trim());
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Veuillez renseigner des valeurs numériques valides.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        labelStatut.setText("Recherche d'offres en cours...");
        modeleTable.setRowCount(0);
        offresCourantes.clear();

        // Exécution en tâche de fond pour ne pas figer l'interface
        new SwingWorker<List<OffreVue>, Void>() 
        {
            @Override
            protected List<OffreVue> doInBackground() throws Exception 
            {
                return service.rechercherOffres(ville, dateArr, dateDep, pers, etoiles, budget);
            }

            @Override
            protected void done() 
            {
                try 
                {
                    List<OffreVue> resultats = get();
                    offresCourantes.addAll(resultats);

                    for (int i = 0; i < resultats.size(); i++) 
                    {
                        OffreVue o = resultats.get(i);
                        modeleTable.addRow(new Object[] {
                            (i + 1),
                            o.nomHotel(),
                            o.nombreEtoiles() + " ★",
                            o.descriptionChambre(),
                            dateArr,
                            dateDep,
                            String.format("%.2f", o.prixTotal())
                        });
                    }

                    if (resultats.isEmpty()) 
                    {
                        labelStatut.setText("Aucune offre trouvée pour ces critères.");
                    } 
                    else 
                    {
                        labelStatut.setText(resultats.size() + " offre(s) trouvée(s) et triée(s) par prix croissant.");
                    }
                } 
                catch (Exception ex) 
                {
                    labelStatut.setText("Erreur : " + ex.getMessage());
                    JOptionPane.showMessageDialog(SwingUI.this, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur SOAP", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void lancerReservation() 
    {
        int ligneChoisie = tableOffres.getSelectedRow();
        if (ligneChoisie < 0 || ligneChoisie >= offresCourantes.size()) 
        {
            JOptionPane.showMessageDialog(this, "Veuillez d'abord sélectionner une offre dans le tableau.", "Sélection requise", JOptionPane.WARNING_MESSAGE);
            return;
        }

        OffreVue offre = offresCourantes.get(ligneChoisie);

        // Formulaire de saisie du client
        JTextField champNom = new JTextField("Dupont", 10);
        JTextField champPrenom = new JTextField("Pierre", 10);
        JTextField champCarte = new JTextField("1234567890123456", 16);
        JTextField champMois = new JTextField("12", 2);
        JTextField champAnnee = new JTextField("2027", 4);
        JTextField champCvc = new JTextField("123", 3);

        JPanel panneauForm = new JPanel(new GridLayout(6, 2, 5, 5));
        panneauForm.add(new JLabel("Nom du client :"));
        panneauForm.add(champNom);
        panneauForm.add(new JLabel("Prénom du client :"));
        panneauForm.add(champPrenom);
        panneauForm.add(new JLabel("Numéro de carte (16 chiffres) :"));
        panneauForm.add(champCarte);
        panneauForm.add(new JLabel("Mois d'expiration (MM) :"));
        panneauForm.add(champMois);
        panneauForm.add(new JLabel("Année d'expiration (AAAA) :"));
        panneauForm.add(champAnnee);
        panneauForm.add(new JLabel("Code CVC :"));
        panneauForm.add(champCvc);

        int option = JOptionPane.showConfirmDialog(
            this,
            panneauForm,
            "Réservation de l'offre #" + (ligneChoisie + 1) + " (" + offre.nomHotel() + " - " + String.format("%.2f €", offre.prixTotal()) + ")",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (option != JOptionPane.OK_OPTION) 
        {
            return;
        }

        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String carte = champCarte.getText().trim();
        int mois;
        int annee;
        String cvc = champCvc.getText().trim();

        try 
        {
            mois = Integer.parseInt(champMois.getText().trim());
            annee = Integer.parseInt(champAnnee.getText().trim());
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Dates d'expiration invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        labelStatut.setText("Traitement de la réservation en cours...");

        new SwingWorker<ResultatReservation, Void>() 
        {
            @Override
            protected ResultatReservation doInBackground() throws Exception 
            {
                return service.reserver(offre, nom, prenom, carte, mois, annee, cvc);
            }

            @Override
            protected void done() 
            {
                try 
                {
                    ResultatReservation res = get();
                    if (res.accepte()) 
                    {
                        labelStatut.setText("Réservation confirmée : " + res.referenceReservation());
                        JOptionPane.showMessageDialog(
                            SwingUI.this,
                            "Félicitations ! Réservation confirmée.\n\n"
                            + "Référence : " + res.referenceReservation() + "\n"
                            + "Message hôtel : " + res.message(),
                            "Réservation Réussie",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        // On relance la recherche pour rafraîchir les disponibilités
                        lancerRecherche();
                    } 
                    else 
                    {
                        String err = (res.refus() != null) ? res.refus() : res.message();
                        labelStatut.setText("Échec de la réservation : " + err);
                        JOptionPane.showMessageDialog(SwingUI.this, "Échec : " + err, "Réservation Refusée", JOptionPane.ERROR_MESSAGE);
                    }
                } 
                catch (Exception ex) 
                {
                    labelStatut.setText("Erreur réseau : " + ex.getMessage());
                    JOptionPane.showMessageDialog(SwingUI.this, "Erreur SOAP lors de la réservation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> {
            String id = args.length > 0 ? args[0] : "agence_lille";
            String secret = args.length > 1 ? args[1] : "secret_lille_123";
            LogiqueAgence service = new LogiqueAgence(id, secret);
            SwingUI ui = new SwingUI(service);
            ui.setVisible(true);
        });
    }
}
