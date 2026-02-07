from __future__ import annotations

import importlib.util
from pathlib import Path


def test_solution_import_and_solve_returns_str() -> None:
    root = Path(__file__).resolve().parents[3]
    p = root / "chapters" / "week_01" / "starter_code" / "solution.py"
    assert p.exists(), f"missing solution.py: {p}"

    spec = importlib.util.spec_from_file_location("week_solution", p)
    assert spec and spec.loader
    m = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(m)  # type: ignore[union-attr]

    assert hasattr(m, "solve")
    out = m.solve("hello")  # type: ignore[attr-defined]
    assert isinstance(out, str)
