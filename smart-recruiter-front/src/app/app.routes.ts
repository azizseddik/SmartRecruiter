import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CandidatsComponent } from './pages/candidats/candidats.component';
import { OffresComponent } from './pages/offres/offres.component';
import { MatchingComponentComponent } from './pages/matching-component/matching-component.component';
import { AjouterOffreComponent } from './pages/ajouter-offre/ajouter-offre.component';
export const routes: Routes = [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'candidats', component: CandidatsComponent },
      { path: 'offres/ajouter', component: AjouterOffreComponent },
      { path: 'offres', component: OffresComponent },
  { path: 'matching', component: MatchingComponentComponent },
  { path: '**', redirectTo: 'dashboard' }
];
