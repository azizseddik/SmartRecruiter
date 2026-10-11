import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
statistiques = [
    {
      titre: 'Total candidats',
      valeur: '128',
      variation: '+12%',
      icone: 'users',
      couleur: 'blue'
    },
    {
      titre: 'Offres actives',
      valeur: '12',
      variation: '+3',
      icone: 'briefcase',
      couleur: 'purple'
    },
    {
      titre: 'CV analysés',
      valeur: '96',
      variation: '+18%',
      icone: 'file',
      couleur: 'orange'
    },
    {
      titre: 'Matchs excellents',
      valeur: '24',
      variation: '+8%',
      icone: 'target',
      couleur: 'green'
    }
  ];

  candidatsRecents = [
    {
      nom: 'Mayssa Esseddik',
      initiales: 'ME',
      poste: 'Technicienne Support IT',
      competences: 'Support IT, CRM',
      score: 92,
      couleur: 'blue'
    },
    {
      nom: 'Ahmed Ben Ali',
      initiales: 'AB',
      poste: 'Ingénieur Cloud',
      competences: 'AWS, Linux, Docker',
      score: 85,
      couleur: 'purple'
    },
    {
      nom: 'Sarra Trabelsi',
      initiales: 'ST',
      poste: 'Administratrice Réseaux',
      competences: 'Cisco, TCP/IP',
      score: 68,
      couleur: 'orange'
    }
  ];
}
