package game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class BribePlayer extends Player {

    private AssetComparator assetComparator = new AssetComparator();
    private static final int MINBRIBE = 5;
    private static final int MAXBRIBE = 10;
    // returns the number of the illegal assets from hand
   public int getNumberOfIllegalAssets() {
     int contorIllegalCards = 0;
       for (Asset a : assetsInHand) {
        if (!a.isLegal()) {
           contorIllegalCards++;
         }
       }
       return contorIllegalCards;
    }

    public void sortAssetsInHand() {
        Collections.sort(assetsInHand, assetComparator);
    }
    // based on the current money, player decides what to do
    // he decides whether he can provide bribe or not
     public void playAsMerchant() {

      int nrIllegalAssets = getNumberOfIllegalAssets();
      List<Asset> listAux = new LinkedList<>();
      // sort in descending order the hand based on profit
      sortAssetsInHand();
      int currentCoins = getCoins();

      if (currentCoins >= MINBRIBE && nrIllegalAssets != 0) {
        if (currentCoins >= MINBRIBE && currentCoins < MAXBRIBE) {
           int contorAdded = 0;
           for (Asset a : assetsInHand) {
               if (!a.isLegal()) {
                   contorAdded++;
                   bag.addInBag(a);
                   listAux.add(a);
                }
             // if he added 2 illegal assets, it's enough for his money
             if (contorAdded == 2) {
                break;
              }
            }
          assetsInHand.removeAll(listAux);
          bag.setBribe(MINBRIBE);

        }
        if (currentCoins >= MAXBRIBE) {
            for (Asset a : assetsInHand) {
             if (!a.isLegal() && !bag.isFull()) {
               bag.addInBag(a);
               listAux.add(a);
   	          }
             }
             assetsInHand.removeAll(listAux);
              if (nrIllegalAssets > 2) {
                 bag.setBribe(MAXBRIBE);
                 decrementCoins(MAXBRIBE);
              } else {
                 bag.setBribe(MINBRIBE);
                 decrementCoins(MINBRIBE);
              }
        }
        bag.setDeclaredType("Apple");
      } else if (currentCoins < MINBRIBE || nrIllegalAssets == 0) {
          // else play as basic player
          super.createBag();
      }
    }
    // inspect players, if bribe does exist, return it to the merchant
   public void inspect(final Player playerStrategy, final Queue<Asset> cards) {
      int bribe = playerStrategy.bag.getBribe();
        if (bribe != 0) {
           playerStrategy.incrementCoins(bribe);
         }

       super.inspect(playerStrategy, cards);
   }
}
