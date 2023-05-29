package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.domain.MstGoods;
import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.mapper.MstGoodsMapper;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;
import jp.co.internous.ecsite.model.mapper.TblPurchaseMapper;

@Controller
@RequestMapping("/ecsite")
public class IndexController {
	
	// MstUserを介してmst_userテーブルにアクセスするためのmapper(DAO)をインスタンス化
	@Autowired
	private MstGoodsMapper goodsMapper;
	
	@Autowired
	private MstUserMapper userMapper;
	
	@Autowired
	private TblPurchaseMapper purchaseMapper;
	
	// JSON形式を扱えるようGsonをインスタンス化しておく
	private Gson gson = new Gson();
	
	// goodsテーブルから取得した商品entityの一覧を、フロントに渡すModelに追加
	@GetMapping("/")
	public String index(Model model) {
		List<MstGoods> goods = goodsMapper.findAll();
		model.addAttribute("goods", goods);
		
		return "index";
	}
	
	@ResponseBody // 文字列そのものが返される
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm f) {
		// mst_userテーブルからユーザー名とパスワードで検索し結果を取得
		MstUser user = userMapper.findByUserNameAndPassword(f);
		
		if (user == null) {
			user = new MstUser();
			user.setFullName("ゲスト");
		}
		// MstUser型のuserをJSON式の文字列に変換し返却
		return gson.toJson(user);	
	}
	
	@ResponseBody
	@PostMapping("/api/purchase")          //   ↓ postObjに入ったデータがfに入る
	public int purchaseApi(@RequestBody CartForm f) {
		
		f.getCartList().forEach((c) -> {
			int total = c.getPrice() * c.getCount();
			purchaseMapper.insert(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});
		
		return f.getCartList().size();
	}
	
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm f) {
		int userId = f.getUserId();
		List<HistoryDto> history = purchaseMapper.findHistory(userId);
		
		return gson.toJson(history);
	}	
	
}
