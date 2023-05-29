// TblPurchaseを介し、tbl_purchaseテーブルにアクセスする
package jp.co.internous.ecsite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.co.internous.ecsite.model.dto.HistoryDto;

@Mapper
public interface TblPurchaseMapper {
	
	// 購入履歴を検索するためのメソッド定義のみ xmlのSELECT文が動く⇒結果をHistoryDto型のListへ格納
	List<HistoryDto> findHistory(int userId);
	
	// 商品の購入情報をデータベースにインサートするメソッド（インターフェースなので処理は実装せず、メソッド定義のみ）
	// メソッドが呼ばれると、SpringBootの機能(MyBatis)により自動的にSQLが実行
	int insert(int userId, int goodsId, String goodsName, int itemCount, int total);
	
}
