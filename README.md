
Méthode  pour évaluer la similitude entre deux signaux  en analysant leurs spectres.

- La corrélation croisée

La corrélation croisée est une technique statistique et mathématique utilisée pour mesurer la similitude ou la dépendance entre deux signaux ou séries temporelles en fonction d’un décalage temporel (appelé lag). 
Elle permet d’analyser comment une série est liée à une autre à différents instants, ce qui est particulièrement utile pour détecter des décalages temporels, des motifs communs ou des relations causales potentielles.


La corrélation croisée consiste à faire glisser un spectre par rapport à un autre et à calculer le coefficient de corrélation à chaque décalage. Cela permet d'identifier le décalage fréquentiel optimal où les spectres sont le plus similaires. 
Cette méthode est sensible aux différences de fréquence, ce qui la rend adaptée à la détection de variations tonales ou de motifs spectraux similaires.

- La densité spectrale croisée.

Pour comparer les spectres de deux signaux, on peut utiliser la densité spectrale croisée. 
Cette méthode permet d'analyser comment les composantes fréquentielles de deux signaux sont liées, ce qui est essentiel dans des domaines tels que la détection de signaux cyclostationnaires ou l'analyse de systèmes dynamiques

- Méthodes de calcul

Méthode de Welch : Utilise la transformée de Fourier pour estimer la densité spectrale croisée.

Méthode de l'accumulation FFT : Permet une estimation efficace de la densité spectrale croisée en utilisant des fenêtres glissantes et la transformée de Fourier rapide .​

- le Dynamic Time Warping (DTW).