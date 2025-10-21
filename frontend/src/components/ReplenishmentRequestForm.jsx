import React, { useState } from "react";
import Swal from "sweetalert2";
import "./ReplenishmentRequestForm.css";

export default function ReplenishmentRequestForm({ onSubmit }) {
  const [note, setNote] = useState("");
  const [items, setItems] = useState([
    { productId: null, productName: "", unit: "", quantity: "", source: "", stock: null, threshold: null }
  ]);
  const [suggestions, setSuggestions] = useState([]);

  // Gọi API lấy gợi ý sản phẩm
  const fetchSuggestions = async (query, index) => {
    if (!query.trim()) {
      setSuggestions([]);
      return;
    }
    try {
      const res = await fetch(`http://localhost:8080/api/products/search?q=${encodeURIComponent(query)}`);
      const data = await res.json();
      // Gắn index vào để biết dòng nào đang autocomplete
      setSuggestions(data.map(p => ({ ...p, rowIndex: index })));
    } catch (err) {
      console.error("Lỗi khi gọi API tìm sản phẩm", err);
    }
  };

  // Khi người dùng chọn một sản phẩm từ danh sách gợi ý
  const handleSelectProduct = (index, product) => {
    const newItems = [...items];
    const suggestedQuantity = Math.max(product.threshold - product.currentStock, 0);
    newItems[index] = {
      productId: product.id,
      productName: product.name,
      unit: product.unit,
      quantity: suggestedQuantity > 0 ? suggestedQuantity : "",
      source: "",
      stock: product.currentStock,
      threshold: product.threshold
    };
    setItems(newItems);
    setSuggestions([]);
  };

  const handleItemChange = (index, field, value) => {
    const newItems = [...items];
    newItems[index][field] = value;
    setItems(newItems);
  };

  const addRow = () => {
    setItems([
      ...items,
      { productId: null, productName: "", unit: "", quantity: "", source: "", stock: null, threshold: null }
    ]);
  };

  const removeRow = (index) => setItems(items.filter((_, i) => i !== index));

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!note.trim()) {
      Swal.fire("⚠️ Thiếu thông tin", "Vui lòng nhập lý do đề nghị bổ sung.", "warning");
      return;
    }

    if (items.some(i => !i.productId || !i.quantity)) {
      Swal.fire("⚠️ Thiếu dữ liệu", "Vui lòng chọn đầy đủ mặt hàng và số lượng.", "warning");
      return;
    }

    const payload = {
      note,
      items: items.map(i => ({
        productId: i.productId,
        productName: i.productName,
        unit: i.unit,
        quantity: Number(i.quantity),
        source: i.source
      }))
    };

    onSubmit(payload);

    Swal.fire({
      title: "✅ Phiếu đã được lập",
      text: "Phiếu đề nghị bổ sung đã được gửi thành công!",
      icon: "success",
      timer: 2000,
      showConfirmButton: false
    });

    setNote("");
    setItems([{ productId: null, productName: "", unit: "", quantity: "", source: "", stock: null, threshold: null }]);
  };

  return (
    <div className="form-container">
      <h2 className="form-title">📥 Lập phiếu đề nghị / dự trù hàng hóa</h2>
      <form onSubmit={handleSubmit} className="form-layout">
        {/* Ghi chú */}
        <div className="form-group">
          <label className="form-label">Ghi chú / Lý do</label>
          <textarea
            className="input-textarea"
            rows={3}
            value={note}
            onChange={(e) => setNote(e.target.value)}
            placeholder="VD: Dự trù thuốc quý II..."
          />
        </div>

        {/* Danh sách mặt hàng */}
        <div className="form-group">
          <label className="form-label item-list-label">Danh sách mặt hàng</label>
          <table className="items-table">
            <thead>
              <tr>
                <th>Tên hàng</th>
                <th>Tồn</th>
                <th>Ngưỡng</th>
                <th>Đơn vị</th>
                <th>SL đề nghị</th>
                <th>Nguồn</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {items.map((item, index) => (
                <tr key={index}>
                  {/* Autocomplete */}
                  <td style={{ position: "relative" }}>
                    <input
                      type="text"
                      value={item.productName}
                      onChange={(e) => {
                        handleItemChange(index, "productName", e.target.value);
                        fetchSuggestions(e.target.value, index);
                      }}
                      className="table-input"
                      placeholder="Nhập tên hàng..."
                    />
                    {suggestions.length > 0 && suggestions[0].rowIndex === index && (
                      <ul className="autocomplete-dropdown">
                        {suggestions.map(s => (
                          <li
                            key={s.id}
                            onClick={() => handleSelectProduct(index, s)}
                            className="autocomplete-item"
                          >
                            {s.name} ({s.unit})
                          </li>
                        ))}
                      </ul>
                    )}
                  </td>

                  {/* Tồn / Ngưỡng / Đơn vị */}
                  <td className="center-text">{item.stock ?? "-"}</td>
                  <td className="center-text">{item.threshold ?? "-"}</td>
                  <td>{item.unit}</td>

                  {/* SL đề nghị */}
                  <td>
                    <input
                      type="number"
                      min="1"
                      value={item.quantity}
                      onChange={(e) => handleItemChange(index, "quantity", e.target.value)}
                      className="table-input"
                    />
                  </td>

                  {/* Nguồn */}
                  <td>
                    <input
                      type="text"
                      value={item.source}
                      onChange={(e) => handleItemChange(index, "source", e.target.value)}
                      className="table-input"
                      placeholder="VD: Ngân sách"
                    />
                  </td>

                  {/* Xóa dòng */}
                  <td>
                    {items.length > 1 && (
                      <button
                        type="button"
                        onClick={() => removeRow(index)}
                        className="btn-remove-row"
                      >
                        Xóa
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          <button type="button" onClick={addRow} className="btn-add-row">
            ➕ Thêm hàng
          </button>
        </div>

        <div className="form-action-group">
          <button type="submit" className="btn-submit">Gửi phiếu đề nghị</button>
        </div>
      </form>
    </div>
  );
}
