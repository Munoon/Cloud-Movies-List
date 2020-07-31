import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { useTable, usePagination } from "react-table";
import Table from "react-bootstrap/Table";
import { fetcher } from "./components/api";
import Pagination from "react-bootstrap/Pagination";
import Dropdown from "react-bootstrap/Dropdown";
import Spinner from "react-bootstrap/Spinner";
import Button from "react-bootstrap/Button";

const getUsers = (page, size) => fetcher(`/users/admin/list?page=${page}&size=${size}`);

const body = () => (
    <UsersTable />
);

function UsersTable(props) {
    const columns = React.useMemo(() => [
        { Header: 'ID', accessor: 'id' },
        { Header: 'Имя', accessor: 'name' },
        { Header: 'Фамилия', accessor: 'surname' },
        { Header: 'Email', accessor: 'email' },
        { Header: 'Роли', accessor: 'roles', Cell: data => data.value.join(', ') }
    ], []);

    const [loading, setLoading] = useState(false);
    const [data, setData] = useState({
        users: [],
        pages: 0
    });

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        page,
        prepareRow,

        canPreviousPage,
        canNextPage,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: { pageIndex, pageSize }
    } = useTable({
        columns,
        data: data.users,
        manualPagination: true,
        pageCount: 3,
        initialState: { pageIndex: data.pages, pageSize: 10 }
    }, usePagination);

    useEffect(() => loadData(), [ pageIndex, pageSize ]);

    const loadData = () => {
        setLoading(true);
        getUsers(pageIndex, pageSize)
            .then(response => setData({
                users: response['_embedded'].users,
                pages: response.page.totalPages
            }))
            .then(() => setLoading(false));
    };

    return (
        <div className='mt-2'>
            <div className='row'>
                <Pagination>
                    <Pagination.First onClick={() => gotoPage(0)} disabled={!canPreviousPage} />
                    <Pagination.Prev onClick={() => previousPage()} disabled={!canPreviousPage} />

                    <Pagination.Item className='span-default-color' disabled>{pageIndex + 1}</Pagination.Item>

                    <Pagination.Next onClick={() => nextPage()} disabled={!canNextPage} />
                    <Pagination.Last onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage} />
                </Pagination>

                <Dropdown className='ml-2'>
                    <Dropdown.Toggle variant='secondary' id='users-table-count-dropdown'>Показывать {pageSize}</Dropdown.Toggle>

                    <Dropdown.Menu>
                        <Dropdown.Item active={pageSize === 5} onClick={() => setPageSize(5)}>Показывать 5</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 10} onClick={() => setPageSize(10)}>Показывать 10</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 15} onClick={() => setPageSize(15)}>Показывать 15</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 20} onClick={() => setPageSize(20)}>Показывать 20</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>

                <div>
                    <Button onClick={() => loadData()} disabled={loading} variant='secondary' className='ml-2 mb-3'>Обновить</Button>
                    {loading && <Spinner animation="border" role="status" className='ml-2 mb-3 spinner-vertical-middle' />}
                </div>
            </div>

            <Table {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                        ))}
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    prepareRow(row);
                    return (
                        <tr key={row.original.id} role='row'>
                            {row.cells.map(cell => (
                                <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            ))}
                        </tr>
                    );
                })}
                </tbody>
            </Table>
        </div>
    );
}

ReactDOM.render(<Application body={body} />, document.getElementById('root'));