package com.jspstudio.community.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jspstudio.community.R
import com.jspstudio.community.base.BaseActivity
import com.jspstudio.community.databinding.ActivityMainBinding
import com.jspstudio.community.view.fragment.KeepStateFragment
import com.jspstudio.community.viewmodel.MainViewModel

// github token : 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

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding.vmMain = viewModel
        binding.lifecycleOwner= this
        setNavigation()
    }
    private fun setNavigation() {

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val navigator = KeepStateFragment(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)

        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.nav_graph)

        // MainActivity의 main_navi와 navController 연결
        binding.mainNavi.setupWithNavController(navController)
    }
}

//import java.util.*;
//import java.util.stream.Collectors;
//
//class Coin {
//    private String market_warning;
//    private String market;
//
//    public Coin(String market_warning, String market) {
//        this.market_warning = market_warning;
//        this.market = market;
//    }
//
//    public String getMarketWarning() {
//        return market_warning;
//    }
//
//    public String getMarket() {
//        return market;
//    }
//
//    // equals()와 hashCode()는 중복 제거 시 market 속성에 대해 동등성을 확인하는 데 필요합니다.
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Coin coin = (Coin) o;
//        return Objects.equals(market, coin.market);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(market);
//    }
//}
//
//public class Main {
//    public static void main(String[] args) {
//        List<Coin> coins = Arrays.asList(
//                new Coin("none", "비트코인"),
//        new Coin("none", "리플"),
//        new Coin("none", "리플"),
//        new Coin("none", "도지")
//        );
//
//        // market 속성에 대해 중복 값을 제거한 새로운 리스트 생성
//        List<Coin> distinctCoins = coins.stream()
//            .collect(Collectors.collectingAndThen(
//                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Coin::getMarket))),
//        ArrayList::new
//        ));
//
//        // 결과 출력
//        System.out.println(distinctCoins.size()); // 3
//    }
//}